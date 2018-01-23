package models

import org.scalatestplus.play.PlaySpec

/** Tests for CardSupplyPile.scala.
  *
  */
class CardSupplyPileSpec extends PlaySpec {

  "The supply pile" should {

    "draw one card from the supply pile" in {
      val supplyWith9cards = Vector(
        (BuildingCard("Archive", 1, "councillor phase", "owner may discard cards from their hand/or cards that were just drawn", 1), 3),
        (BuildingCard("Gold Mine", 1, "prospector phase", "owner draws 4 cards and, if all have different building costs, keeps the cheapest", 1), 3),
        (BuildingCard("Smithy", 1, "builder phase", "owner pays 1 card less when building production buildings", 1), 3)
      )

      val (drawnCards, updatedSupplyPile) = CardSupplyPile.drawNCardsFromPile(1, supplyWith9cards)
      drawnCards.size mustBe 1
      updatedSupplyPile.map(_._2).sum mustBe 8
    }

    "draw many cards from the supply pile and return an updated supply pile" in {
      val supplyWith9cards = Vector(
        (BuildingCard("Archive", 1, "councillor phase", "owner may discard cards from their hand/or cards that were just drawn", 1), 3),
        (BuildingCard("Gold Mine", 1, "prospector phase", "owner draws 4 cards and, if all have different building costs, keeps the cheapest", 1), 3),
        (BuildingCard("Smithy", 1, "builder phase", "owner pays 1 card less when building production buildings", 1), 3)
      )
      val (_, updatedSupplyPile) = CardSupplyPile.drawNCardsFromPile(3, supplyWith9cards)
      updatedSupplyPile.map(_._2).sum mustBe 6
      // draw a few other cards with the updated supply
      val (_, updatedSupplyPile2) = CardSupplyPile.drawNCardsFromPile(2, updatedSupplyPile)
      updatedSupplyPile2.map(_._2).sum mustBe 4
    }
  }
}
