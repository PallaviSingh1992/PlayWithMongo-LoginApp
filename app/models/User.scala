package models

import reactivemongo.api.collections.bson.BSONCollection
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration._
import reactivemongo.bson.BSONDocument
import services.ConnectMongo


case class User(id: Int, name: String, password: String, email: String)

class UserAuthentication {

  val db = ConnectMongo.db.collection[BSONCollection]("users_credentials")

  def register(user: User): Boolean = {

    val query = BSONDocument("email" -> user.email)
    val isUserValid = Await.result(db.find(query).one[BSONDocument], 25.seconds)
    if (!isUserValid.isDefined) {
      try {
        val userBson = BSONDocument("id" -> user.id, "name" -> user.name, "password" -> user.password, "email" -> user.email)
        db.insert(userBson)
        true
      }
      catch {
        case exception: Exception => false
      }
    }
    else {
      false
    }
  }

  def login(email: String, password: String): Boolean = {

    val query = BSONDocument("email" -> email, "password" -> password)
    val userDetailsValid = Await.result(db.find(query).one[BSONDocument], 25.seconds)
    if (userDetailsValid.isDefined) {
      true
    }
    else {
      false
    }
  }

}