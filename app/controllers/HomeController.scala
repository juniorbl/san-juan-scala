package controllers

import javax.inject._

import models.SanJuan
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, sanJuan: SanJuan) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  /** Handles the action to create a game.
    *
    * FIXME get the player name from the request
    */
  def createGame = Action.async {
    sanJuan.createGameWithPlayer("carlos").map { roleCardsAndPlayer =>
      Ok(Json.obj(
        "roleCards" -> roleCardsAndPlayer._1,
        "player" -> roleCardsAndPlayer._2
      ))
    }.recover {
      case _: reactivemongo.core.actors.Exceptions.PrimaryUnavailableException =>
        InternalServerError("Error: database not available")
      case error => InternalServerError(s"Error while creating game: ${error.getMessage}")
    }
  }
}
