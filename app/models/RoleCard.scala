package models

import play.api.libs.json.Json

/** A role card in the game.
  *
  * @param name the card's name
  * @param action the action that will be performed by all players, including the holder of the card
  * @param privilege the benefit that only the holder of the card gets
  */
case class RoleCard(name: String, action: String, privilege: String)

object RoleCard {

  implicit val gameFormat = Json.format[RoleCard]

  /** Creates all the role cards of the game.
    *
    * @return a list of the role cards
    */
  def createRoleCards(): List[RoleCard] = {
    List(
      RoleCard("Builder", "each player can build one building", "builder pays one card less"),
      RoleCard("Trader", "each player can sell one good", "trader can sell one additional good"),
      RoleCard("Councillor", "each player draws 2 cards and chooses 1 to keep", "councillor draws an additional 3 cards"),
      RoleCard("Prospector", "none", "prospector draws one card"),
      RoleCard("Producer", "each player can produce one good", "producer can produce one additional good")
    )
  }
}