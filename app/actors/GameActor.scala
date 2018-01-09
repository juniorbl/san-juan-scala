package actors

import models.{BuildingCard, CardSupplyPile, Game}
import akka.actor.{Actor, ActorRef, Props}
import play.api.Logger
import play.api.libs.json.Json

import scala.util.Random

/** Actor that receives game messages.
  *
  * FIXME remove the simple console logs and use ActorLogging
  */
class GameActor(out: ActorRef) extends Actor {

  // FIXME send the Json object instead of its string representation

  override def receive = {
    case message @ "buildRoleCards" =>
      Logger.info(s"message received [$message] to actor [${this.getClass.getName}]")
      out ! Json.obj("cardType" -> "role", "cards" -> Game.createRoleCards()).toString()

    case message @ "drawCard" =>
      Logger.info(s"message received [$message] to actor [${this.getClass.getName}]")
      val supplyPile: Vector[(BuildingCard, Int)] = CardSupplyPile.buildInitialPile()
      // FIXME it should return the updated supply pile as well
      val (cardDrawn, updatedSupplyPile) = CardSupplyPile.drawCard(
        Random.nextInt(supplyPile.map(_._2).sum),
        supplyPile)
      out ! Json.toJson(cardDrawn).toString()
  }

  override def preStart(): Unit = {
    Logger.info(s"starting actor [${this.getClass.getName}]")
  }

  override def postStop(): Unit = {
    Logger.info(s"actor stopped [${this.getClass.getName}]")
  }
}

object GameActor {

  // props is used to create the actor
  def props(out: ActorRef) = Props(new GameActor(out))
}