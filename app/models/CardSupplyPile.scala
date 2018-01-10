package models

import scala.annotation.tailrec

/** Logic related to the card supply pile in the game.
  *
  */
object CardSupplyPile {

  /** Builds the initial card supply pile. Instead of duplicate data to represent the quantity of the same card in the pile (ex: 3 instances of Archive card, 3 of Gold Mine cards, etc),
    * the card pile has the cards and their quantity in the pile (ex: [Archive card, 3 units], [Gold Mine card, 3 units] etc).
    *
    * @return a pile of building cards
    */
  def buildInitialPile(): Vector[(BuildingCard, Int)] = {
    Vector(
      (BuildingCard("Archive", 1, "councillor phase", "owner may discard cards from their hand/or cards that were just drawn", 1), 3),
      (BuildingCard("Gold Mine", 1, "prospector phase", "owner draws 4 cards and, if all have different building costs, keeps the cheapest", 1), 3),
      (BuildingCard("Smithy", 1, "builder phase", "owner pays 1 card less when building production buildings", 1), 3),
      (BuildingCard("Office building", 1, "beginning of a round", "owner may discard 1 or 2 cards and then draw 1 or 2 new cards", 1), 3)
    )
  }

  /** Represents drawing a card from the supply pile, returning a new pile with updated values. To represent the randomness of the drawing, the index should be randomly generated.
    * Ex: given the supply pile [Archive card, 3 units], [Gold Mine card, 3 units], drawing a card at index 4 would return:
    * the card: Gold Mine card
    * the updated supply pile: [Archive card, 3 units], [Gold Mine card, 2 units]
    *
    * @param cardIndexToTake the index of the pile where the card should be taken, from 0 to the size of the pile
    * @param supplyPile the supply pile from where the card should be drawn
    * @return a tuple with the card drawn and the updated supply pile
    */
  def drawCard(cardIndexToTake: Int, supplyPile: Vector[(BuildingCard, Int)]): (BuildingCard, Vector[(BuildingCard, Int)]) = {

    // Use binary search to find the card at the given index, repeatedly dividing the pile vector in half until it finds the index, ex:
    // if a vector represents the following supply pile:
    // (card A, 2 units)
    // (card B, 3 units)
    // (card C, 3 units)
    // the card type at index 5 would be B, at index 6 would be C, and so on.
    @tailrec
    def findCardIndexInPile(numberOfCardsUpToThisPoint: Int, someSupplyPile: Vector[(BuildingCard, Int)]): (BuildingCard, Int) = {
      val (firstHalfPile, secondHalfPile) = someSupplyPile.splitAt(someSupplyPile.size / 2)
      val numberOfCardsUpToThisPlusFirstHalf = numberOfCardsUpToThisPoint + firstHalfPile.map(_._2).sum
      if (numberOfCardsUpToThisPlusFirstHalf > cardIndexToTake && someSupplyPile.size != 1) {
        findCardIndexInPile(numberOfCardsUpToThisPoint, firstHalfPile)
      } else if (numberOfCardsUpToThisPlusFirstHalf < cardIndexToTake && someSupplyPile.size != 1) {
        findCardIndexInPile(numberOfCardsUpToThisPlusFirstHalf, secondHalfPile)
      } else {
        someSupplyPile(0)
      }
    }

    val (cardAtIndex, quantity) = findCardIndexInPile(0, supplyPile)
    val vectorIndexWhereCardWasDrawn = supplyPile.indexOf((cardAtIndex, quantity))
    val updatedSupplyPile = supplyPile.updated(vectorIndexWhereCardWasDrawn, (cardAtIndex, quantity - 1))
    (cardAtIndex, updatedSupplyPile)
  }
}
