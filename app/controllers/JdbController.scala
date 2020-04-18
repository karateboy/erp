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
class JdbController @Inject()(cc: ControllerComponents, customerOps: CustomerOps)
                             (implicit assetsFinder: AssetsFinder) extends Authentication(cc) {

  def listCustomer() = Authenticated.async {
    for (ret <- customerOps.list()) yield {
      val jsons = ret.map {doc => doc.toJson()}
      Ok(Json.toJson(jsons))
    }
  }
}
