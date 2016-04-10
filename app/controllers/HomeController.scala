package controllers

import javax.inject._
import com.google.inject.Inject
import models.{User, UserAuthentication}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}
import play.api.i18n.Messages.Implicits._
import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject() extends Controller {

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */

  val userInstance = new UserAuthentication

  val userForm = Form {
    mapping(
      "id" -> number,
      "name" -> nonEmptyText,
      "password" -> nonEmptyText,
      "email" -> nonEmptyText
    )(User.apply)(User.unapply)
  }

  val loginForm = Form {
    tuple(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    )
  }

  def loadLogin = Action { implicit request =>
    Ok(views.html.login(loginForm))
  }

  def submitLogin = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      badForm => BadRequest(views.html.login(loginForm)),
      validUserData => {
        val isUserValid = userInstance.login(validUserData._1, validUserData._2)
        isUserValid match {
          case true  => Ok(views.html.success())
          case false => Ok(views.html.failure())
        }
      }
    )
  }

  def loadRegistration = Action { implicit request =>
    Ok(views.html.registration(userForm))
  }

  def submitRegistration = Action { implicit request =>
    userForm.bindFromRequest.fold(
      badForm => BadRequest(views.html.registration(userForm)),
      validData => {
        val isRegister = userInstance.register(validData)

        isRegister match {
          case true  => Ok(views.html.login(loginForm))
          case false => Ok(views.html.regfailure())
        }
      }
    )
  }

}
