package models

/** A building card in the game.
  *
  * @param name the name of the card
  * @param cost the cost of the card
  * @param abilityPhase the phase that the card can use its ability
  * @param ability the ability the card offers to its holder
  * @param victoryPoints the number of victory points the card has
  */
case class BuildingCard(name: String, cost: Int, abilityPhase: String, ability: String, victoryPoints: Int) {
}
