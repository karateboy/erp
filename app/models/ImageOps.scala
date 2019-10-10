package models
import java.util.Date

import javax.inject.{Inject, Singleton}
import play.api._
import com.typesafe.config._

import scala.concurrent._
import scala.collection.JavaConverters._
import org.mongodb.scala.model._
import org.mongodb.scala.model.Indexes._
import org.mongodb.scala.bson._
import ModelHelper._

import scala.concurrent.ExecutionContext.Implicits.global

case class Image(_id: ObjectId, fileName: String, tags:Seq[String], date:Long, content: Array[Byte])
@Singleton
class ImageOps @Inject() (mongoDB: MongoDB) {
  import org.mongodb.scala.bson.codecs.Macros._
  import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
  import org.bson.codecs.configuration.CodecRegistries.{ fromRegistries, fromProviders }

  val codecRegistry = fromRegistries(fromProviders(classOf[Image]), DEFAULT_CODEC_REGISTRY)

  val NAME = "image"
  val collection = mongoDB.database.getCollection[Image](NAME).withCodecRegistry(codecRegistry)
  collection.createIndex(Indexes.ascending("tags")).toFuture().failed.foreach(errorHandler)

  def get(objId: ObjectId) = {
    val f = collection.find(Filters.eq("_id", objId)).first().toFuture()
    f.failed.foreach(errorHandler)
    f
  }
}