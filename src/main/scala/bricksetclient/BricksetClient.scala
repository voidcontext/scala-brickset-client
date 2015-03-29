package bricksetclient

import scala.collection.JavaConversions._
import bricksetproduceractor._
import com.brickset.api._

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

case class CArrayOfSets(aos: ArrayOfSets)

class BricksetClient(val apiKey: String) {
  // create an actor system
  private val sys = ActorSystem("BricksetClient")

  // instantiate a BricksetProducerActor actor
  private val actor = sys.actorOf(Props[BricksetProducerActor], "bricksetproduceractor")

  // user hash
  private var userHash: String = ""

  // set promise timmeouts
  implicit val timeout = Timeout(60 seconds)

  /**
   * Builds a BricksetRequest, and automatically adds the apikey param
   */
  private def buildRequest(operation: String, params: List[String]):  BricksetRequest = {
    new BricksetRequest(apiKey :: params, Map(
        "operationName" -> operation
      ))
  }

  /**
   * Checks the current api key
   */
  def checkKey(): Future[String] = {
    (actor ? buildRequest("checkKey", List())).mapTo[String]
  }

  def login(username: String, password: String): Future[String] = {
    val future = (actor ? buildRequest("login", List(username, password))).mapTo[String]

    future onSuccess {
      case uh : String => userHash = uh
    }

    future
  }

  def getOwnedSets() : Future[Seq[Sets]] = {
    (actor ? buildRequest("getSets", List(userHash, "", "", "", "", "", "1", "", "", "", "", "")))
      .mapTo[Seq[Sets]]

  }
}


