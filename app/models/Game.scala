package models

import scala.util.Random

/** The central game logic.
  *
  */
object Game {

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

  /** Perform the privilege action of the Prospector card: the player gets one card from the supply pile.
    *
    * @param playerHand the player hand
    * @param supplyPile the game supply pile
    * @return a tuple with the updated player hand and the updated supply pile
    */
  def prospect(playerHand: List[BuildingCard], supplyPile: Vector[(BuildingCard, Int)]): (List[BuildingCard], Vector[(BuildingCard, Int)]) = {
    val (cardDrawn, updatedSupplyPile) = CardSupplyPile.drawCard(
      Random.nextInt(supplyPile.map(_._2).sum),
      supplyPile)
    val updatedPlayerHand = cardDrawn :: playerHand
    (updatedPlayerHand, updatedSupplyPile)
  }
}
