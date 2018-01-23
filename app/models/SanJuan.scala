package models

import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}

/** Represents a game and its information.
  *
  * @param activePlayer the player active at a given moment
  * @param roleCards the role cards
  * @param cardSupplyPile the supply of cards and its quantity
  */
case class Game(activePlayer: Player, roleCards: List[RoleCard], cardSupplyPile: Vector[(BuildingCard, Int)])

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
    * @return a tuple with the role cards and the newly created player
    */
  def createGameWithPlayer(startingPlayerName: String): Future[(List[RoleCard], Player)] = {
    val (playerHand, updatedCardSupplyPile) = CardSupplyPile.drawNCardsFromPile(4, CardSupplyPile.buildInitialPile())
    val startingPlayer = Player.createPlayer(startingPlayerName, playerHand)
    val roleCards = createRoleCards()
    val game = Game(startingPlayer, roleCards, updatedCardSupplyPile)
    gameRepository.saveGame(game).map { _ =>
      (roleCards, startingPlayer)
    }
  }

  /** Creates all the role cards of the game.
    *
    * @return a list of the role cards
    */
  private[models] def createRoleCards(): List[RoleCard] = {
    List(
      RoleCard("Builder", "each player can build one building", "builder pays one card less"),
      RoleCard("Trader", "each player can sell one good", "trader can sell one additional good"),
      RoleCard("Councillor", "each player draws 2 cards and chooses 1 to keep", "councillor draws an additional 3 cards"),
      RoleCard("Prospector", "none", "prospector draws one card"),
      RoleCard("Producer", "each player can produce one good", "producer can produce one additional good")
    )
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

