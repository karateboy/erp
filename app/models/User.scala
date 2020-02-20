package models

import com.mongodb.client.model.UpdateOptions
import javax.inject.{Inject, Singleton}
import play.api._
import com.typesafe.config._
import models.ModelHelper.errorHandler
import org.mongodb.scala.model.{Filters, Indexes, ReplaceOptions, Updates}

import scala.concurrent.ExecutionContext.Implicits.global
import models.ModelHelper._
import org.bson.conversions.Bson
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.bson.conversions.Bson

case class User(var _id: String, password: String, groups: Seq[String], name: String)

@Singleton
class UserDB @Inject()(mongoDB: MongoDB) {

  import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
  import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
  import org.mongodb.scala.bson.codecs.Macros._

  val codecRegistry = fromRegistries(fromProviders(classOf[User]), DEFAULT_CODEC_REGISTRY)

  val NAME = "users"
  val collection = mongoDB.database.getCollection[User](NAME).withCodecRegistry(codecRegistry)
  collection.createIndex(Indexes.ascending("groups")).toFuture().failed.foreach(errorHandler)

  for (count <- collection.countDocuments().toFuture()) {
    //Insert default user
    if (count == 0) {
      collection.insertOne(User("user", "abc123", Seq("admin"), "user")).toFuture()

    }
  }


  def getUserById(_id: String) = {

    import org.mongodb.scala.model._

    val f = collection.find(Filters.equal("_id", _id)).limit(1).toFuture()
    f.failed.foreach(errorHandler())
    f
  }

  def getUserList() = {
    val f = collection.find(Filters.exists("_id")).toFuture()
    f.failed.foreach(errorHandler())
    f
  }

  def upsert(user: User) = {
    val f = collection.replaceOne(Filters.eq("_id", user._id),
      user, ReplaceOptions().upsert(true)).toFuture()
    f.failed.foreach(errorHandler())
    f
  }

  def delete(_id:String) = {
    val f = collection.deleteOne(Filters.equal("_id", _id)).toFuture()
    f.failed.foreach(errorHandler())
    f
  }
}