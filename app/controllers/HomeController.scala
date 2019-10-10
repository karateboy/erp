package controllers

import javax.inject._

import play.api.mvc._
import models._
import play._
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json._
import collection.JavaConverters
import models.ObjectIdUtil._
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, imageOps: ImageOps)(implicit assetsFinder: AssetsFinder)
  extends Authentication(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Redirect("/app/index.html")
  }

  def getOwnerLessImage = Authenticated.async {
    val f = imageOps.getNoOwner()(5)
    for (ret <- f) yield {
      implicit val writes = Json.writes[Image]
      Ok(Json.toJson(ret))
    }
  }
}
