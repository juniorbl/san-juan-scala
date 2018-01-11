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

    "build a building card using the same number of cards as the cost of the card to build when the player DOES NOT have the Builder role card" in {
      val cardToBuild = BuildingCard("Poor house", 2, "builder phase", "owner draws a card only if holding 0 cards or 1 card after building", 1)
      val playerHand = List( // discard 2 cards for a card to build with cost 2
        BuildingCard("Archive", 1, "councillor phase", "owner may discard cards from their hand/or cards that were just drawn", 1, true),
        BuildingCard("Gold Mine", 1, "prospector phase", "owner draws 4 cards and, if all have different building costs, keeps the cheapest", 1, true),
        BuildingCard("Smithy", 1, "builder phase", "owner pays 1 card less when building production buildings", 1)
      )
      val buildResult = Game.build(cardToBuild, playerHand, false)
      buildResult mustBe Right(
        BuildingCard("Poor house", 2, "builder phase", "owner draws a card only if holding 0 cards or 1 card after building", 1),
        List(
          BuildingCard("Smithy", 1, "builder phase", "owner pays 1 card less when building production buildings", 1)
        )
      )
    }

    "build a building card using the card to build cost minus 1 when the player DOES have the Builder role card" in {
      val cardToBuild = BuildingCard("Poor house", 2, "builder phase", "owner draws a card only if holding 0 cards or 1 card after building", 1)
      val playerHand = List( // discard 1 card for a card to build with cost 2
        BuildingCard("Archive", 1, "councillor phase", "owner may discard cards from their hand/or cards that were just drawn", 1, true),
        BuildingCard("Gold Mine", 1, "prospector phase", "owner draws 4 cards and, if all have different building costs, keeps the cheapest", 1),
        BuildingCard("Smithy", 1, "builder phase", "owner pays 1 card less when building production buildings", 1)
      )
      val buildResult = Game.build(cardToBuild, playerHand, true)
      buildResult mustBe Right(
        BuildingCard("Poor house", 2, "builder phase", "owner draws a card only if holding 0 cards or 1 card after building", 1),
        List(
          BuildingCard("Gold Mine", 1, "prospector phase", "owner draws 4 cards and, if all have different building costs, keeps the cheapest", 1),
          BuildingCard("Smithy", 1, "builder phase", "owner pays 1 card less when building production buildings", 1)
        )
      )
    }

    "return an error message when the player discards less cards than the cost of the card to build" in {
      val cardToBuild = BuildingCard("Tower", 3, "beginning of a round", "owner may keep up to 12 cards", 2)
      val playerHand = List(
        BuildingCard("Archive", 1, "councillor phase", "owner may discard cards from their hand/or cards that were just drawn", 1, true),
        BuildingCard("Gold Mine", 1, "prospector phase", "owner draws 4 cards and, if all have different building costs, keeps the cheapest", 1),
        BuildingCard("Smithy", 1, "builder phase", "owner pays 1 card less when building production buildings", 1)
      )
      val buildResult = Game.build(cardToBuild, playerHand, false)
      buildResult.isLeft mustBe true // just checking if the result is Left, the contents of the message is not important
    }
  }
}
