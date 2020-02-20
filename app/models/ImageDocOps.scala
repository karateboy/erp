package models

import java.util.Date

import javax.inject.{Inject, Singleton}
import models.ModelHelper._
import org.mongodb.scala.bson._
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class ImageDoc(_id: ObjectId, tags: Seq[String], date: Date, text: String,
                    images: Seq[ObjectId], var log: Seq[String])

case class ShortDocJson(_id: String, tags: Seq[String], dateTime: Long)

@Singleton
class ImageDocOps @Inject()(configOps: ConfigOps, mongoDB: MongoDB) {

  import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
  import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
  import org.mongodb.scala.bson.codecs.Macros._

  val codecRegistry = fromRegistries(fromProviders(classOf[ImageDoc]), DEFAULT_CODEC_REGISTRY)

  val NAME = "imageDoc"
  val collection = mongoDB.database.getCollection[ImageDoc](NAME).withCodecRegistry(codecRegistry)
  collection.createIndex(Indexes.ascending("tags")).toFuture().failed.foreach(errorHandler)
  collection.createIndex(Indexes.ascending("date")).toFuture().failed.foreach(errorHandler)
  collection.createIndex(Indexes.ascending("images")).toFuture().failed.foreach(errorHandler)
  collection.createIndex(Indexes.ascending("text")).toFuture().failed.foreach(errorHandler)

  def get(objId: ObjectId): Future[ImageDoc] = {
    val f = collection.find(Filters.eq("_id", objId)).first().toFuture()
    f.failed.foreach(errorHandler)
    f
  }

  import scala.collection.JavaConverters._

  def query(filter: Bson)(skip: Int)(limit: Int) = {
    val proj = Projections.include("_id", "tags", "date")
    val f = mongoDB.database.getCollection(NAME).find(filter).projection(proj).sort(Sorts.descending("date")).skip(skip).limit(limit).toFuture()
    f.failed.foreach(errorHandler)
    for (docs <- f) yield {
      docs map { doc =>
        val _id = doc("_id").asObjectId().getValue.toHexString
        val tags = doc("tags").asArray().getValues.asScala.map {
          _.asString().getValue
        }
        val date = doc("date").asDateTime().getValue
        ShortDocJson(_id, tags, date)
      }
    }
  }

  def insertOne(doc: ImageDoc) = {
    val configF = configOps.updateTags(doc.tags)
    configF.failed.foreach(errorHandler)
    val f = collection.insertOne(doc).toFuture()
    f.failed.foreach(errorHandler)
    f
  }

  def upsert(doc: ImageDoc) = {
    val f = collection.replaceOne(Filters.equal("_id", doc._id), doc, ReplaceOptions().upsert(true)).toFuture()
    f.failed.foreach(errorHandler)
    f
  }

  def attachImage(docId:ObjectId, imageId:ObjectId, logEntry:String) = {
    val updates = Updates.combine(
      Updates.addToSet("images", imageId),
      Updates.pushEach("log", PushOptions().position(0), logEntry)
    )
    val f = collection.findOneAndUpdate(Filters.equal("_id", docId), updates).toFuture()
    f.failed.foreach(errorHandler)
    f
  }
}
