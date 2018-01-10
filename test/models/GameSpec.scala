package models

import org.scalatestplus.play._

/** Tests for Game.scala.
  *
  */
class GameSpec extends PlaySpec {

  "The game" should {

    "have 5 unique role cards" in {
      val roleCards = Game.createRoleCards()
      roleCards.size mustBe 5
      roleCards.distinct.size mustBe 5
    }

    "add a new building card to the player hand during the Prospector phase and return an updated supply pile" in {
      val playerHand = List(
        BuildingCard("Archive", 1, "councillor phase", "owner may discard cards from their hand/or cards that were just drawn", 1),
        BuildingCard("Gold Mine", 1, "prospector phase", "owner draws 4 cards and, if all have different building costs, keeps the cheapest", 1)
      )
      val supplyPile = Vector( // 9 cards
        (BuildingCard("Archive", 1, "councillor phase", "owner may discard cards from their hand/or cards that were just drawn", 1), 3),
        (BuildingCard("Gold Mine", 1, "prospector phase", "owner draws 4 cards and, if all have different building costs, keeps the cheapest", 1), 3),
        (BuildingCard("Smithy", 1, "builder phase", "owner pays 1 card less when building production buildings", 1), 3)
      )
      val (updatedPlayerHand, updatedSupplyPile) = Game.prospect(playerHand, supplyPile)
      updatedPlayerHand.size mustBe 3
      updatedSupplyPile.map(_._2).sum mustBe 8
    }
  }
}
