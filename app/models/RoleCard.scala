package models

import play.api.libs.json.{JsValue, Json, Writes}

/** A role card in the game.
  *
  * @param name the card's name
  * @param action the action that will be performed by all players, including the holder of the card
  * @param privilege the benefit that only the holder of the card gets
  */
case class RoleCard(name: String, action: String, privilege: String)

object RoleCard {

  // conversion from role card to json representation
  implicit val roleCardWrites = new Writes[RoleCard] {
    override def writes(rc: RoleCard): JsValue = {
      Json.obj(
        "name" -> rc.name,
        "action" -> rc.action,
        "privilege" -> rc.privilege
      )
    }
  }
}