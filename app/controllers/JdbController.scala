package controllers

import java.io.{File, FileOutputStream}
import java.nio.file.Files
import java.time.LocalDateTime
import java.util.Date

import javax.inject._
import models.ModelHelper._
import models._
import org.bson.types.ObjectId
import org.mongodb.scala.bson.conversions.Bson
import play.api.Logger
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class JdbController @Inject()(cc: ControllerComponents, customerOps: CustomerOps, productOps: ProductOps,
                             orderOps: OrderOps, workOps:WorkOps)
                             (implicit assetsFinder: AssetsFinder) extends Authentication(cc) {

  def listCustomer() = Authenticated.async {
    for (ret <- customerOps.list()) yield {
      val jsons = ret.map {doc => doc.toJson()}
      Ok(Json.toJson(jsons))
    }
  }

  def listProduct() = Authenticated.async {
    for (ret <- productOps.list()) yield {
      val jsons = ret.map {doc => doc.toJson()}
      Ok(Json.toJson(jsons))
    }
  }

  def listOrder() = Authenticated.async {
    for (ret <- orderOps.list()) yield {
      val jsons = ret.map {doc => doc.toJson()}
      Ok(Json.toJson(jsons))
    }
  }

  def listWork() = Authenticated.async {
    for (ret <- workOps.list()) yield {
      val jsons = ret.map {doc => doc.toJson()}
      Ok(Json.toJson(jsons))
    }
  }
}
