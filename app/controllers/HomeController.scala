package controllers

import java.io.{File, FileOutputStream}
import java.nio.file.Files
import java.time.LocalDateTime
import java.util.Date

import javax.inject._
import models._
import org.bson.types.ObjectId
import org.mongodb.scala.bson.conversions.Bson
import play.api.Logger
import play.api.libs.json._
import play.api.mvc._
import models.ModelHelper._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, imageOps: ImageOps, docOps: ImageDocOps, configOps: ConfigOps)
                              (implicit assetsFinder: AssetsFinder) extends Authentication(cc) {

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
      val creatorInfo = request.user
      import java.time._
      implicit val paramReads = Json.reads[NewImageDocParam]
      val paramRet = request.body.validate[NewImageDocParam]
      paramRet.fold(
        error =>
          Future {
            Logger.error("")
            BadRequest(Json.obj("ok" -> false, "msg" -> JsError.toJson(error)))
          }
        , valid = param => {

          import org.mongodb.scala.bson._

          val _id = if (param._id.isEmpty)
            new ObjectId()
          else
            new ObjectId(param._id)

          val images = param.mergeImageId map {
            new ObjectId(_)
          }
          val updateOwnerF = imageOps.updateOwner(_id, images)
          val now = LocalDateTime.now()
          val createdMsg = s"${LocalDateTime.now()} ${creatorInfo.name} created."
          val doc = ImageDoc(_id, param.tags, new Date(), "",
            images, Seq(createdMsg))

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

  def searchDoc(tags: String, start: Option[Long], end: Option[Long], skip: Int, limit: Int) = Authenticated.async {

    import org.mongodb.scala.model._

    val tagFilter: Option[Bson] =
      if (tags.isEmpty)
        Some(Filters.exists("_id"))
      else {
        val tagList = tags.split(",")
        Some(Filters.all("tags", tagList: _*))
      }

    val startFilter: Option[Bson] = start map { dt =>
      Filters.gte("date", new Date(dt))
    }

    val endFilter = end map { dt =>
      Filters.lte("date", new Date(dt))
    }


    val filter = Filters.and(Seq(tagFilter, startFilter, endFilter).flatten: _*)
    for (docList <- docOps.query(filter)(skip)(limit)) yield {
      //val ids = docList map ( doc=> doc("_id").asObjectId().getValue.toHexString)
      implicit val write = Json.writes[ShortDocJson]
      Ok(Json.toJson(docList))
    }
  }

  def getImageParams(idList: String) = Authenticated.async {
    implicit val writes = Json.writes[ImageParam]
    val objIdList = idList.split(",") map {
      new ObjectId(_)
    }
    val f = imageOps.getImagesParam(objIdList)
    for (ret <- f) yield
      Ok(Json.toJson(ret))
  }

  def upsertDoc() = Authenticated.async(cc.parsers.json) {
    implicit request =>
      val updater = request.user
      implicit val reads = Json.reads[ImageDoc]
      import models.ObjectIdUtil._
      val docRet = request.body.validate[ImageDoc]
      docRet.fold(
        error =>
          Future {
            Logger.error(JsError.toJson(error).toString())
            BadRequest(Json.obj("ok" -> false, "msg" -> JsError.toJson(error)))
          }
        , valid = doc => {
          val now = LocalDateTime.now()
          val updateMsg = s"${LocalDateTime.now()} ${updater.name} updated."
          doc.log = updateMsg +: doc.log
          for (ret <- docOps.upsert(doc)) yield
            Ok(Json.obj("ok" -> (ret.getModifiedCount == 1)))
        })
  }

  def attachImageToDoc(docIdStr: String) = Authenticated(parse.multipartFormData) {
    implicit request =>
      val updater = request.user
      Logger.info("handle attached upload")
      request.body.file("image").map { picture =>
        import java.io.File
        val filename: String = picture.filename
        val contentType = picture.contentType

        val logEntry = s"${LocalDateTime.now()} ${updater.name} upload ${filename}"
        val docId = new ObjectId(docIdStr)
        val imgIdF = imageOps.importPath(path = picture.ref.path,
          tags = Seq("upload"), owner = Some(docId), originalName = filename)
        val imgId = waitReadyResult(imgIdF)
        docOps.attachImage(docId, imgId, logEntry)
      }

      Ok(Json.obj("ok" -> true))
  }
}
