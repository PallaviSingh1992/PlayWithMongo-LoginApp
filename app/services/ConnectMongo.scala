package services

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object ConnectMongo {

  val driver = new reactivemongo.api.MongoDriver
  val connection = driver.connection(List("localhost"))
  val db = Await.result(connection.database("user"), 25.seconds)

}
