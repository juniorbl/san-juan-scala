package models

import org.scalatestplus.play.PlaySpec

/** Tests for CardSupplyPile.scala.
  *
  */
class CardSupplyPileSpec extends PlaySpec {

  "The supply pile" should {

    "return the correct card when drawing a card at various indexes" in {
      val supplyPile = Vector(
        (BuildingCard("Archive", 1, "councillor phase", "owner may discard cards from their hand/or cards that were just drawn", 1), 3),
        (BuildingCard("Gold Mine", 1, "prospector phase", "owner draws 4 cards and, if all have different building costs, keeps the cheapest", 1), 3),
        (BuildingCard("Smithy", 1, "builder phase", "owner pays 1 card less when building production buildings", 1), 3)
      )

      val (drawnCard, updatedSupplyPile) = CardSupplyPile.drawCard(3, supplyPile)
      drawnCard mustBe BuildingCard("Archive", 1, "councillor phase", "owner may discard cards from their hand/or cards that were just drawn", 1)
      updatedSupplyPile mustBe
        Vector(
          (BuildingCard("Archive", 1, "councillor phase", "owner may discard cards from their hand/or cards that were just drawn", 1), 2), // decreased quantity of the card drawn
          (BuildingCard("Gold Mine", 1, "prospector phase", "owner draws 4 cards and, if all have different building costs, keeps the cheapest", 1), 3),
          (BuildingCard("Smithy", 1, "builder phase", "owner pays 1 card less when building production buildings", 1), 3)
        )

      val (drawnCard2, updatedSupplyPile2) = CardSupplyPile.drawCard(5, supplyPile)
      drawnCard2 mustBe BuildingCard("Gold Mine", 1, "prospector phase", "owner draws 4 cards and, if all have different building costs, keeps the cheapest", 1)
      updatedSupplyPile2 mustBe
        Vector(
          (BuildingCard("Archive", 1, "councillor phase", "owner may discard cards from their hand/or cards that were just drawn", 1), 3),
          (BuildingCard("Gold Mine", 1, "prospector phase", "owner draws 4 cards and, if all have different building costs, keeps the cheapest", 1), 2), // decreased quantity of the card drawn
          (BuildingCard("Smithy", 1, "builder phase", "owner pays 1 card less when building production buildings", 1), 3)
        )

      val (drawnCard3, updatedSupplyPile3) = CardSupplyPile.drawCard(7, supplyPile)
      drawnCard3 mustBe BuildingCard("Smithy", 1, "builder phase", "owner pays 1 card less when building production buildings", 1)
      updatedSupplyPile3 mustBe
        Vector(
          (BuildingCard("Archive", 1, "councillor phase", "owner may discard cards from their hand/or cards that were just drawn", 1), 3),
          (BuildingCard("Gold Mine", 1, "prospector phase", "owner draws 4 cards and, if all have different building costs, keeps the cheapest", 1), 3),
          (BuildingCard("Smithy", 1, "builder phase", "owner pays 1 card less when building production buildings", 1), 2) // decreased quantity of the card drawn
        )
    }

    "return an updated supply pile after drawing cards" in {
      val supplyWith9cards = Vector( // 9 cards
        (BuildingCard("Archive", 1, "councillor phase", "owner may discard cards from their hand/or cards that were just drawn", 1), 3),
        (BuildingCard("Gold Mine", 1, "prospector phase", "owner draws 4 cards and, if all have different building costs, keeps the cheapest", 1), 3),
        (BuildingCard("Smithy", 1, "builder phase", "owner pays 1 card less when building production buildings", 1), 3)
      )
      // draw one card
      val (_, updatedSupplyPile) = CardSupplyPile.drawCard(3, supplyWith9cards)
      updatedSupplyPile.map(_._2).sum mustBe 8
      // draw another card with the updated supply
      val (_, updatedSupplyPile2) = CardSupplyPile.drawCard(6, updatedSupplyPile)
      updatedSupplyPile2.map(_._2).sum mustBe 7
    }
  }
}
