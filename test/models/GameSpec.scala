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
  }
}
