package models

import play.api.libs.json.Json

/** A player in the game.
  *
  * @param name the name of the player
  * @param hand the hand of cards of the player
  * @param buildings the buildings of the player
  */
case class Player(name: String, hand: List[BuildingCard], buildings: List[BuildingCard])

object Player {

  implicit val playerFormat = Json.format[Player]

  /** Created a player.
    *
    * @param name the name of the player
    * @param hand the hand of the player
    * @return a newly created player
    */
  def createPlayer(name: String, hand: List[BuildingCard]): Player = {
    // Every player starts with an indigo plan building
    Player(name, hand, List(BuildingCard("Indigo plant", 1, "producer phase", "owner produces 1 indigo", 1)))
  }
}