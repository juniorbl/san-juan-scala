package models

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

  /** Draws a number of cards from a given deck.
    *
    * @param quantity the quantity that should be drawn from the deck
    * @param deck the deck the cards should be drawn from
    * @return the new deck without the drawn cards
    */
  def drawCards(quantity: Int, deck: List[BuildingCard]): (List[BuildingCard], List[BuildingCard]) = {
    deck.splitAt(quantity)
  }
}
