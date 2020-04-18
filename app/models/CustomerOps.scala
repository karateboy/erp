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
import org.mongodb.scala.{Document, MongoCollection}
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.bson.conversions.Bson

@Singleton
class CustomerOps @Inject()(mongoDB: MongoDB) {

  import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
  import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
  import org.mongodb.scala.bson.codecs.Macros._

  //val codecRegistry = fromRegistries(fromProviders(classOf[User]), DEFAULT_CODEC_REGISTRY)

  val NAME = "jtCustomer"
  val collection: MongoCollection[Document] = mongoDB.database.getCollection(NAME)

  def list() = {
    val f = collection.find(Filters.exists("_id")).toFuture()
    f.failed.foreach(errorHandler())
    f
  }

  def upsert(customer: Document) = {
    val f = collection.replaceOne(Filters.eq("_id", customer("_id")),
      customer, ReplaceOptions().upsert(true)).toFuture()
    f.failed.foreach(errorHandler())
    f
  }

  def delete(_id:String) = {
    val f = collection.deleteOne(Filters.equal("_id", _id)).toFuture()
    f.failed.foreach(errorHandler())
    f
  }
}