package controllers

import javax.inject._
import models._
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
case class Credential(userName: String, password: String)

/**
 * @author user
 */
@Singleton
class Login @Inject() (userDB: UserDB, cc: ControllerComponents)(implicit assetsFinder: AssetsFinder)
  extends Authentication(cc) {
  implicit val credentialReads = Json.reads[Credential]
  implicit val userWrites = Json.writes[User]

  def authenticate = Action(cc.parsers.json).async {
    implicit request =>
      val credential = request.body.validate[Credential]
      credential.fold(

          error =>
            Future {
              BadRequest(Json.obj("ok" -> false, "msg" -> JsError.toJson(error)))
            }
        ,
        crd => {
          val f = userDB.getUserById(crd.userName)
          for(ret <- f)yield {
            if(ret.isEmpty|| ret(0).password != crd.password)
              Ok(Json.obj("ok" -> false))
            else{
              val user = ret(0)
              implicit val userInfoWrite = Json.writes[UserInfo]
              val userInfo = UserInfo(user._id, user._id)
              Ok(Json.obj("ok" -> true, "user" -> userInfo)).withSession(setUserinfo(request, userInfo))
            }
          }
        })
  }

  def logout = Action {
    Ok(Json.obj(("ok" -> true))).withNewSession
  }

  def getUserInfo = Authenticated.async {
    implicit request =>
      val userInfo = request.user
      for(ret <- userDB.getUserById(userInfo.id))yield
        Ok(Json.toJson(ret.head))
  }
}