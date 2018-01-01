package models

/** A role card in the game.
  *
  * @param name the card's name
  * @param action the action that will be performed by all players, including the holder of the card
  * @param privilege the benefit that only the holder of the card gets
  */
case class RoleCard(name: String, action: String, privilege: String) {
}
