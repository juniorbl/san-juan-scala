package models

import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}

/** Represents a game and its information.
  *
  * @param activePlayerName the name of the player active at a given moment
  * @param players the players in the game
  * @param roleCards the role cards
  * @param cardSupplyPile the supply of cards and its quantity
  */
case class Game(activePlayerName: String, players: List[Player], roleCards: List[RoleCard], cardSupplyPile: Vector[(BuildingCard, Int)])

object Game {
  import play.api.libs.json.Json

  implicit val gameFormat = Json.format[Game]
}

/** Central logic of the game.
  *
  * @param gameRepository the game repository
  */
@Singleton
class SanJuan @Inject()(implicit execContext: ExecutionContext, gameRepository: GameRepository) {

  /** Creates a game and its first player.
    *
    * @param startingPlayerName the name of the first player
    * @return a future with a tuple with the role cards and the newly created player
    */
  def createGameWithPlayer(startingPlayerName: String): Future[(List[RoleCard], Player)] = {
    val (playerHand, updatedCardSupplyPile) = CardSupplyPile.drawNCardsFromPile(4, CardSupplyPile.buildInitialPile())
    val startingPlayer = Player.createPlayer(startingPlayerName, playerHand)
    val roleCards = RoleCard.createRoleCards()
    val game = Game(startingPlayer.name, List(startingPlayer), roleCards, updatedCardSupplyPile)
    gameRepository.saveGame(game).map { _ =>
      (roleCards, startingPlayer)
    }
  }

  /** Perform the privilege action of the Prospector card: the player gets one card from the supply pile.
    *
    * @param playerHand the player hand
    * @param supplyPile the game supply pile
    * @return a tuple with the updated player hand and the updated supply pile
    */
  def prospect(playerHand: List[BuildingCard], supplyPile: Vector[(BuildingCard, Int)]): (List[BuildingCard], Vector[(BuildingCard, Int)]) = {
    val (cardsDrawn, updatedSupplyPile) = CardSupplyPile.drawNCardsFromPile(1, supplyPile)
    val updatedPlayerHand = cardsDrawn.head :: playerHand
    (updatedPlayerHand, updatedSupplyPile)
  }

  /** Perform the action of the Builder card: build a building by receiving the number of cards described in the cost of the card to build.
    *
    * @param cardToBuild the card that will be built
    * @param playerHand the player hand with the cards that should be discarded
    * @param privilege whether or not the action is a privilege, if true the cost of the card to build decreases by 1 card
    * @return Either: Right - the building card that was built and the updated player hand, or Left - an error message
    */
  def build(cardToBuild: BuildingCard, playerHand: List[BuildingCard], privilege: Boolean): Either[String, (BuildingCard, List[BuildingCard])] = {
    val cardToBuildCost = if (privilege) cardToBuild.cost - 1 else cardToBuild.cost
    if (playerHand.filter(_.discard).size == cardToBuildCost) {
      val updatedPlayerHand = playerHand.filterNot(_.discard)
      Right(cardToBuild, updatedPlayerHand)
    } else {
      Left(s"Insufficient cards to build ${cardToBuild}")
    }
  }
}

