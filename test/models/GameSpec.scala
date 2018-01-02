package models

import org.scalatestplus.play._

/** Tests for Game.scala.
  *
  */
class GameSpec extends PlaySpec {

  "The game" should {

    "have 5 and unique role cards" in {
      val roleCards = Game.createRoleCards()
      roleCards.size mustBe 5
      roleCards.distinct.size mustBe 5
    }

    "drawn the cards from the top of the deck" in {
      val deck = List(
        BuildingCard("Archive", 1, "councillor phase", "owner may discard cards from their hand/or cards that were just drawn", 1),
        BuildingCard("Gold Mine", 1, "prospector phase", "owner draws 4 cards and, if all have different building costs, keeps the cheapest", 1),
        BuildingCard("Smithy", 1, "builder phase", "owner pays 1 card less when building production buildings", 1),
        BuildingCard("Office building", 1, "beginning of a round", "owner may discard 1 or 2 cards and then draw 1 or 2 new cards", 1)
      )
      val (drawnCards, newDeck) = Game.drawCards(2, deck)
      drawnCards mustBe List(
        BuildingCard("Archive", 1, "councillor phase", "owner may discard cards from their hand/or cards that were just drawn", 1),
        BuildingCard("Gold Mine", 1, "prospector phase", "owner draws 4 cards and, if all have different building costs, keeps the cheapest", 1)
      )
      newDeck mustBe List(
        BuildingCard("Smithy", 1, "builder phase", "owner pays 1 card less when building production buildings", 1),
        BuildingCard("Office building", 1, "beginning of a round", "owner may discard 1 or 2 cards and then draw 1 or 2 new cards", 1)
      )
    }
  }
}
