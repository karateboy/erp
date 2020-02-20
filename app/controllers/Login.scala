package controllers

import javax.inject._
import models._
import org.bson.types.ObjectId
import play.api.Logger
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

case class Credential(userName: String, password: String)

/**
 * @author user
 */
@Singleton
class Login @Inject()(userDB: UserDB, groupOps: GroupOps, cc: ControllerComponents)(implicit assetsFinder: AssetsFinder)
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
          for (ret <- f) yield {
            if (ret.isEmpty || ret(0).password != crd.password)
              Ok(Json.obj("ok" -> false))
            else {
              val user = ret(0)
              implicit val userInfoWrite = Json.writes[UserInfo]
              val userInfo = UserInfo(user._id, user.name)
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
      val userInfo: UserInfo = request.user
      for (ret: Seq[User] <- userDB.getUserById(userInfo.id)) yield
        Ok(Json.toJson(ret.head))
  }

  def getUserList = Authenticated.async {
    implicit request =>
      for (userList <- userDB.getUserList()) yield {
        val idNameList = userList map {
          usr => {
            Json.obj("_id" -> usr._id, "name" -> usr.name)
          }
        }
        Ok(Json.toJson(idNameList))
      }
  }

  def getGroupList = Authenticated.async {
    implicit request =>
      for (groupList <- groupOps.getGroupList()) yield {
        implicit val writes = Json.writes[Group]
        Ok(Json.toJson(groupList))
      }
  }

  def upsertUser = Authenticated(cc.parsers.json).async {
    implicit request =>
      implicit val reads = Json.reads[User]
      val ret = request.body.validate[User]
      ret.fold((err: Seq[(JsPath, Seq[JsonValidationError])]) =>
        Future {
          Logger.error(JsError.toJson(err).toString)
          BadRequest(JsError.toJson(err).toString)
        },
        user => {
          for (ret <- userDB.upsert(user)) yield
            Ok(Json.obj("ok" -> true))
        })
  }

  def deleteUser(id: String) = Authenticated.async {
    implicit request =>
      val f = userDB.delete(id)
      for (ret <- f) yield
        Ok(Json.obj("ok" -> (ret.getDeletedCount() == 1)))
  }

  def getUser(_id: String) = Authenticated.async {
      val f: Future[Seq[User]] = userDB.getUserById(_id)
      for (usr <- f) yield
        Ok(Json.toJson(usr.head))

  }
}