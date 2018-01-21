package models

import javax.inject.Inject

import play.api.Logger
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json.collection.{JSONCollection, _}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/** Repository for the game.
  *
  * @param mongoApi the mongo that will be used
  */
class GameRepository @Inject()(mongoApi: ReactiveMongoApi) {

  import Game._

  def collection: Future[JSONCollection] = mongoApi.database.map(_.collection[JSONCollection]("sjsCollection"))

  /** Saves a given game.
    *
    * @param game the game to be saved
    * @return an unit future
    */
  def saveGame(game: Game): Future[Unit] = {
    collection.flatMap(_.insert(game)
      .map(_ => Logger.info("Game saved successfully")))
  }
}
