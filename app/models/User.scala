package models

import javax.inject.{Inject, Singleton}
import play.api._
import com.typesafe.config._
import models.ModelHelper.errorHandler
import org.mongodb.scala.model.Indexes
import scala.concurrent.ExecutionContext.Implicits.global
import models.ModelHelper._

case class User(_id: String, password: String, groups: Seq[String])

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
      collection.insertOne(User("user", "abc123", Seq("admin"))).toFuture()

    }
  }


  def getUserById(_id: String) = {

    import org.mongodb.scala.model._

    val f = collection.find(Filters.equal("_id", _id)).toFuture()
    f.failed.foreach(errorHandler())
    f
  }

  def getUserList() = {
    import org.mongodb.scala.model._
    val f = collection.find(Filters.exists("_id")).toFuture()
    f.failed.foreach(errorHandler())
    f
  }
}