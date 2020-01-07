package controllers

import javax.inject._
import models._
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
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

  import models.ObjectIdUtil._
  def getOwnerLessImage = Authenticated.async {
    val f = imageOps.getNoOwner()(10)
    for (ret <- f) yield {
      implicit val writes = Json.writes[Image]
      Ok(Json.toJson(ret))
    }
  }
}
