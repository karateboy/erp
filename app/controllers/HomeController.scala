package controllers

import java.io.{File, FileOutputStream}
import java.nio.file.Files
import java.util.Date

import javax.inject._
import models._
import org.bson.types.ObjectId
import play.api.Logger
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, imageOps: ImageOps, docOps: ImageDocOps, configOps: ConfigOps)(implicit assetsFinder: AssetsFinder)
  extends Authentication(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Redirect("/app/index.html")
  }

  import models.ObjectIdUtil._

  def getOwnerLessImage(skip: Int) = Authenticated.async {
    val f = imageOps.getNoOwner()(skip, 12)
    for (ret <- f) yield {
      implicit val writes = Json.writes[ImageParam]
      Ok(Json.toJson(ret))
    }
  }

  def getImage(id: String) = Authenticated.async {
    import org.mongodb.scala.bson._
    val objectId = new ObjectId(id)
    val f: Future[Image] = imageOps.get(objectId)
    for (image <- f) yield {
      val contentType =
        if (image.fileName.endsWith("jpeg") || image.fileName.endsWith("jpg"))
          "image/jpeg"
        else if (image.fileName.endsWith("png"))
          "image/png"
        else if (image.fileName.endsWith("bmp"))
          "image/bmp"
        else if (image.fileName.endsWith("pdf"))
          "application/pdf"
        else
          "image/*"

      if (!image.fileName.endsWith("xlsx"))
        Ok(image.content).as(contentType)
      else {
        val retFilePath = Files.createTempFile("temp", ".xlsx")
        val retFile = new File(retFilePath.toAbsolutePath().toString())
        val fo = new FileOutputStream(retFile)
        fo.write(image.content)
        fo.close()
        Ok.sendFile(retFile,
          onClose = () => {
            Files.deleteIfExists(retFile.toPath())
          })
      }
    }
  }

  def getFile(fileName: String) = Authenticated.async {
    import org.mongodb.scala.bson._
    val idStr = fileName.takeWhile(_ != '.')
    val objectId = new ObjectId(idStr)
    val f: Future[Image] = imageOps.get(objectId)
    for (image <- f) yield {
      val retFilePath = Files.createTempFile("temp", ".xlsx")
      val retFile = new File(retFilePath.toAbsolutePath().toString())
      val fo = new FileOutputStream(retFile)
      fo.write(image.content)
      fo.close()
      Ok.sendFile(retFile,
        onClose = () => {
          Files.deleteIfExists(retFile.toPath())
        })
    }
  }


  case class NewImageDocParam(_id: String, mergeImageId: Seq[String], tags: Seq[String])

  def newImageDoc() = Authenticated.async(cc.parsers.json) {
    implicit request =>
      implicit val paramReads = Json.reads[NewImageDocParam]
      val paramRet = request.body.validate[NewImageDocParam]
      paramRet.fold(
        error =>
          Future {
            Logger.error("")
            BadRequest(Json.obj("ok" -> false, "msg" -> JsError.toJson(error)))
          }
        , param => {

          import org.mongodb.scala.bson._

          val _id = if (param._id.isEmpty)
            new ObjectId()
          else
            new ObjectId(param._id)

          val images = param.mergeImageId map {
            new ObjectId(_)
          }
          val updateOwnerF = imageOps.updateOwner(_id, images)
          val doc = ImageDoc(_id, param.tags, new Date(), "",
            images)

          for {
            retImageUpdate <- updateOwnerF
            retDoc <- docOps.insertOne(doc)
          }
            yield {
              Logger.info(retImageUpdate.toString)
              Logger.info(retDoc.toString())
              Ok(Json.obj("ok" -> true))
            }
        })
  }

  def getTags = Authenticated.async {
    for (tags <- configOps.getTags) yield {
      Ok(Json.toJson(tags))
    }
  }

  def getDoc(_id: String) = Authenticated.async {

    import org.mongodb.scala.bson._

    for (doc <- docOps.get(new ObjectId(_id))) yield {
      //val ids = docList map ( doc=> doc("_id").asObjectId().getValue.toHexString)
      implicit val write = Json.writes[ImageDoc]
      Ok(Json.toJson(doc))
    }
  }

  def searchDoc(tags: String, skip: Int, limit: Int) = Authenticated.async {

    import org.mongodb.scala.model._

    val tagFilter =
      if (tags.isEmpty)
        Filters.exists("_id")
      else {
        val tagList = tags.split(",")
        Filters.all("tags", tagList: _*)
      }


    for (docList <- docOps.query(tagFilter)(skip)(limit)) yield {
      //val ids = docList map ( doc=> doc("_id").asObjectId().getValue.toHexString)
      implicit val write = Json.writes[ShortDocJson]
      Ok(Json.toJson(docList))
    }
  }

  def getImageParams(idList: String) = Authenticated.async {
    implicit val writes = Json.writes[ImageParam]
    val objIdList = idList.split(",") map { new ObjectId(_)}
    val f = imageOps.getImagesParam(objIdList)
    for(ret <- f) yield
      Ok(Json.toJson(ret))
  }
}
