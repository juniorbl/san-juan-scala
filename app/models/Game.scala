package models

import scala.annotation.tailrec

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

  /** Builds the initial card stack. Instead of duplicate data to represent the quantity of the same card in the stack (ex: 3 instances of Archive card, 3 of Gold Mine cards, etc),
    * the card stack has the cards and their quantity in the stack (ex: [Archive card, 3 units], [Gold Mine card, 3 units] etc).
    *
    * @return a stack of building cards
    */
  def buildInitialCardStack(): Vector[(BuildingCard, Int)] = {
    Vector(
      (BuildingCard("Archive", 1, "councillor phase", "owner may discard cards from their hand/or cards that were just drawn", 1), 3),
      (BuildingCard("Gold Mine", 1, "prospector phase", "owner draws 4 cards and, if all have different building costs, keeps the cheapest", 1), 3),
      (BuildingCard("Smithy", 1, "builder phase", "owner pays 1 card less when building production buildings", 1), 3),
      (BuildingCard("Office building", 1, "beginning of a round", "owner may discard 1 or 2 cards and then draw 1 or 2 new cards", 1), 3)
    )
  }

  /** Represents drawing a card from the stack, returning a new stack with updated values. To represent the randomness of the drawing, the index should randomly generated.
    * Ex: given the card stack [Archive card, 3 units], [Gold Mine card, 3 units], drawing a card at index 4 would return:
    * the card: Gold Mine card
    * the updated card stack: [Archive card, 3 units], [Gold Mine card, 2 units]
    *
    * @param cardIndexToTake the index of the stack where the card should be taken, from 0 to the size of the stack
    * @param cardStack the card stack from where the card should be drawn
    * @return a tuple with the card drawn and the new card stack
    */
  def drawCardFromStack(cardIndexToTake: Int, cardStack: Vector[(BuildingCard, Int)]): (BuildingCard, Vector[(BuildingCard, Int)]) = {

    // Use binary search to find the card at the given index, repeatedly dividing the card stack vector in half until it finds the index, ex:
    // if a vector represents the following card stack:
    // (card A, 2 units)
    // (card B, 3 units)
    // (card C, 3 units)
    // the card type at index 5 would be B, at index 6 would be C, and so on.
    @tailrec
    def findCardInStack(numberOfCardsUpToThisPoint: Int, someCardStack: Vector[(BuildingCard, Int)]): (BuildingCard, Int) = {
      val (firstHalfCardStack, secondHalfCardStack) = someCardStack.splitAt(someCardStack.size / 2)
      val numberOfCardsUpToThisPlusFirstHalf = numberOfCardsUpToThisPoint + firstHalfCardStack.map(_._2).sum
      if (numberOfCardsUpToThisPlusFirstHalf > cardIndexToTake && someCardStack.size != 1) {
        findCardInStack(numberOfCardsUpToThisPoint, firstHalfCardStack)
      } else if (numberOfCardsUpToThisPlusFirstHalf < cardIndexToTake && someCardStack.size != 1) {
        findCardInStack(numberOfCardsUpToThisPlusFirstHalf, secondHalfCardStack)
      } else {
        someCardStack(0)
      }
    }

    val (cardAtIndex, quantity) = findCardInStack(0, cardStack)
    val vectorIndexWhereCardWasDrawn = cardStack.indexOf((cardAtIndex, quantity))
    val updatedCardStack = cardStack.updated(vectorIndexWhereCardWasDrawn, (cardAtIndex, quantity - 1))
    (cardAtIndex, updatedCardStack)
  }
}
