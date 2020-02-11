package models

import java.util.Date

import javax.inject.{Inject, Singleton}
import models.ModelHelper._
import org.mongodb.scala.bson._
import org.mongodb.scala.model._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.collection.JavaConverters._

case class Image(_id: ObjectId, fileName: String, tags: Seq[String], date: Date,
                 content: Array[Byte], owner: Option[ObjectId] = None)

case class ImageParam(_id: String, fileName: String, tags: Seq[String])

@Singleton
class ImageOps @Inject()(mongoDB: MongoDB) {

  import org.mongodb.scala.bson.codecs.Macros

  val imageProvider = Macros.createCodecProvider[Image]()

  import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
  import org.bson.codecs.configuration.CodecRegistries.{fromRegistries, fromProviders}

  val codecRegistry = fromRegistries(DEFAULT_CODEC_REGISTRY, fromProviders(imageProvider))

  val NAME = "image"
  val collection = mongoDB.database.getCollection[Image](NAME).withCodecRegistry(codecRegistry)
  collection.createIndex(Indexes.ascending("tags")).toFuture().failed.foreach(errorHandler)
  collection.createIndex(Indexes.ascending("date")).toFuture().failed.foreach(errorHandler)
  collection.createIndex(Indexes.ascending("owner")).toFuture().failed.foreach(errorHandler)

  def get(objId: ObjectId): Future[Image] = {
    val f = collection.find(Filters.eq("_id", objId)).first().toFuture()
    f.failed.foreach(errorHandler)
    f
  }

  def getNoOwner()(skip: Int, limit: Int): Future[Seq[ImageParam]] = {
    val projection = Projections.include("_id", "tags", "fileName")
    val f = mongoDB.database.getCollection(NAME).find(Filters.equal("owner", null))
      .projection(projection)
      .sort(Sorts.ascending("date"))
      .skip(skip)
      .limit(limit).toFuture()
    f.failed.foreach(errorHandler)
    for (docs <- f) yield {
      docs map { doc =>
        val _id = doc.getObjectId("_id").toHexString
        val tags = doc("tags").asArray().asScala.map(_.asString().getValue).toSeq
        val fileName = doc.getString("fileName")
        ImageParam(_id = _id, tags = tags, fileName = fileName)
      }
    }
  }

  def updateOwner(ownerId: ObjectId, imageIds: Seq[ObjectId]) = {
    val f = collection.updateMany(Filters.in("_id", imageIds: _*), Updates.set("owner", ownerId)).toFuture()
    f.failed.foreach(errorHandler)
    f
  }

  def getImagesParam(idList: Seq[ObjectId]) = {
    val projection = Projections.include("_id", "tags", "fileName")
    val f = mongoDB.database.getCollection(NAME).find(Filters.in("_id", idList:_*))
      .projection(projection).toFuture()
    f.failed.foreach(errorHandler)
    for (docs <- f) yield {
      val ret = for(id <- idList) yield {
        for(found <- docs.find(doc=>doc.getObjectId("_id") == id)) yield {
          val tags = found("tags").asArray().asScala.map(_.asString().getValue).toSeq
          val fileName = found.getString("fileName")
          ImageParam(_id = id.toHexString, tags = tags, fileName = fileName)
        }
      }
      ret flatMap {x=>x}
    }
  }
}