package models

import javax.inject.{Inject, Singleton}
import models.ModelHelper.errorHandler
import org.mongodb.scala.model.Indexes
import play.api._

import scala.concurrent.ExecutionContext.Implicits.global

case class Group(_id: String, name:String)

@Singleton
class GroupOps @Inject()(mongoDB: MongoDB) {

  import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
  import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
  import org.mongodb.scala.bson.codecs.Macros._

  val codecRegistry = fromRegistries(fromProviders(classOf[Group]), DEFAULT_CODEC_REGISTRY)

  val NAME = "groups"
  val collection = mongoDB.database.getCollection[Group](NAME).withCodecRegistry(codecRegistry)
  collection.createIndex(Indexes.ascending("groups")).toFuture().failed.foreach(errorHandler)

  for (count <- collection.countDocuments().toFuture()) {
    //Insert default group
    if (count == 0) {
      collection.insertOne(Group("admin", "admin")).toFuture()
    }
  }

  import org.mongodb.scala.model._
  def getGroupById(_id: String) = {
    val f = collection.find(Filters.equal("_id", _id)).toFuture()
    f.failed.foreach(errorHandler())
    f
  }

  def getGroupList() = {
    val f = collection.find(Filters.exists("_id")).toFuture()
    f.failed.foreach(errorHandler())
    f
  }
}