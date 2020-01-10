package models

import java.util.Date

import javax.inject.{Inject, Singleton}
import models.ModelHelper._
import org.mongodb.scala.bson._
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class Config(_id: ObjectId, tags: Seq[String])

@Singleton
class ConfigOps @Inject()(mongoDB: MongoDB) {

  import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
  import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
  import org.mongodb.scala.bson.codecs.Macros._

  val codecRegistry = fromRegistries(fromProviders(classOf[Config]), DEFAULT_CODEC_REGISTRY)

  val NAME = "config"
  val collection = mongoDB.database.getCollection[Config](NAME).withCodecRegistry(codecRegistry)
  for (ret <- collection.countDocuments(Filters.exists("_id")).toFuture()) {
    if (ret == 0) {
      collection.insertOne(Config(new ObjectId(), Seq.empty)).toFuture()
    }
  }

  def get(): Future[Config] = {
    val f = collection.find(Filters.exists("_id")).first().toFuture()
    f.failed.foreach(errorHandler)
    f
  }

  def updateTags(tags : Seq[String]) = {
    val f = collection.updateOne(Filters.exists("_id"), Updates.addEachToSet("tags", tags:_*)).toFuture()
    f.failed.foreach(errorHandler)
    f
  }

  def getTags: Future[Seq[String]] = {
    for(ret <- get()) yield
      ret.tags
  }
}
