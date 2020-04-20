package models

import javax.inject.{Inject, Singleton}
import models.ModelHelper.errorHandler
import org.mongodb.scala.model.{Filters, ReplaceOptions}
import org.mongodb.scala.{Document, MongoCollection}

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class ProductOps @Inject()(mongoDB: MongoDB) {

  //val codecRegistry = fromRegistries(fromProviders(classOf[User]), DEFAULT_CODEC_REGISTRY)

  val NAME = "jtProduct"
  val collection: MongoCollection[Document] = mongoDB.database.getCollection(NAME)

  def list() = {
    val f = collection.find(Filters.exists("_id")).limit(500).toFuture()
    f.failed.foreach(errorHandler())
    f
  }

  def upsert(doc: Document) = {
    val f = collection.replaceOne(Filters.eq("_id", doc("_id")),
      doc, ReplaceOptions().upsert(true)).toFuture()
    f.failed.foreach(errorHandler())
    f
  }

  def delete(_id:String) = {
    val f = collection.deleteOne(Filters.equal("_id", _id)).toFuture()
    f.failed.foreach(errorHandler())
    f
  }
}