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
                    images: Seq[ObjectId], imageTypes: Seq[String])

@Singleton
class ImageDocOps @Inject()(mongoDB: MongoDB) {

  import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
  import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
  import org.mongodb.scala.bson.codecs.Macros._

  val codecRegistry = fromRegistries(fromProviders(classOf[ImageDoc]), DEFAULT_CODEC_REGISTRY)

  val NAME = "imageDoc"
  val collection = mongoDB.database.getCollection[Image](NAME).withCodecRegistry(codecRegistry)
  collection.createIndex(Indexes.ascending("tags")).toFuture().failed.foreach(errorHandler)
  collection.createIndex(Indexes.ascending("date")).toFuture().failed.foreach(errorHandler)
  collection.createIndex(Indexes.ascending("images")).toFuture().failed.foreach(errorHandler)
  collection.createIndex(Indexes.ascending("text")).toFuture().failed.foreach(errorHandler)

  def get(objId: ObjectId): Future[Image] = {
    val f = collection.find(Filters.eq("_id", objId)).first().toFuture()
    f.failed.foreach(errorHandler)
    f
  }

  def query(filter: Bson)(limit: Int) = {
    val f = collection.find(filter).sort(Sorts.descending("date")).limit(limit).toFuture()
    f.failed.foreach(errorHandler)
    f
  }
}
