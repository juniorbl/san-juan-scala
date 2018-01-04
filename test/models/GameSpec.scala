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

    "drawn the correct card at various indexes" in {
      val cardStack = Vector(
        (BuildingCard("Archive", 1, "councillor phase", "owner may discard cards from their hand/or cards that were just drawn", 1), 3),
        (BuildingCard("Gold Mine", 1, "prospector phase", "owner draws 4 cards and, if all have different building costs, keeps the cheapest", 1), 3),
        (BuildingCard("Smithy", 1, "builder phase", "owner pays 1 card less when building production buildings", 1), 3)
      )

      val (card, newCardStack) = Game.drawCardFromStack(3, cardStack)
      card mustBe BuildingCard("Archive", 1, "councillor phase", "owner may discard cards from their hand/or cards that were just drawn", 1)
      newCardStack mustBe
        Vector(
          (BuildingCard("Archive", 1, "councillor phase", "owner may discard cards from their hand/or cards that were just drawn", 1), 2), // decreased quantity of the card drawn
          (BuildingCard("Gold Mine", 1, "prospector phase", "owner draws 4 cards and, if all have different building costs, keeps the cheapest", 1), 3),
          (BuildingCard("Smithy", 1, "builder phase", "owner pays 1 card less when building production buildings", 1), 3)
        )

      val (card2, newCardStack2) = Game.drawCardFromStack(5, cardStack)
      card2 mustBe BuildingCard("Gold Mine", 1, "prospector phase", "owner draws 4 cards and, if all have different building costs, keeps the cheapest", 1)
      newCardStack2 mustBe
        Vector(
          (BuildingCard("Archive", 1, "councillor phase", "owner may discard cards from their hand/or cards that were just drawn", 1), 3),
          (BuildingCard("Gold Mine", 1, "prospector phase", "owner draws 4 cards and, if all have different building costs, keeps the cheapest", 1), 2), // decreased quantity of the card drawn
          (BuildingCard("Smithy", 1, "builder phase", "owner pays 1 card less when building production buildings", 1), 3)
        )

      val (card3, newCardStack3) = Game.drawCardFromStack(7, cardStack)
      card3 mustBe BuildingCard("Smithy", 1, "builder phase", "owner pays 1 card less when building production buildings", 1)
      newCardStack3 mustBe
        Vector(
          (BuildingCard("Archive", 1, "councillor phase", "owner may discard cards from their hand/or cards that were just drawn", 1), 3),
          (BuildingCard("Gold Mine", 1, "prospector phase", "owner draws 4 cards and, if all have different building costs, keeps the cheapest", 1), 3),
          (BuildingCard("Smithy", 1, "builder phase", "owner pays 1 card less when building production buildings", 1), 2) // decreased quantity of the card drawn
        )
    }
  }
}
