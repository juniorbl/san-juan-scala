package controllers

import javax.inject._

import actors.GameActor
import akka.actor._
import akka.stream.Materializer

import play.api.libs.streams.ActorFlow
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents)(implicit actorSystem: ActorSystem, mat: Materializer) extends AbstractController(cc) {

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

  /**
    * Create a game actor that will handle the game WebSocket connection.
    *
    */
  def game = WebSocket.accept[String, String] {
    // FIXME must handle broadcast a message to all actors
    request =>
      ActorFlow.actorRef {
        out => GameActor.props(out)
      }
  }
}
