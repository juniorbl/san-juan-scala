package models

import play.api.libs.json.{JsValue, Json, Writes}

/** A building card in the game.
  *
  * @param name the name of the card
  * @param cost the cost of the card
  * @param abilityPhase the phase that the card can use its ability
  * @param ability the ability the card offers to its holder
  * @param victoryPoints the number of victory points the card has
  */
case class BuildingCard(name: String, cost: Int, abilityPhase: String, ability: String, victoryPoints: Int)

object BuildingCard {

  // conversion from building card to json representation
  implicit val buildingCardWrites = new Writes[BuildingCard] {
    override def writes(bc: BuildingCard): JsValue = {
      Json.obj("cardType" -> "supply",
        "cards" -> Json.arr(
          Json.obj(
            "name" -> bc.name,
            "cost" -> bc.cost,
            "abilityPhase" -> bc.abilityPhase,
            "ability" -> bc.ability,
            "victoryPoints" -> bc.victoryPoints
          )
        )
      )
    }
  }
}


