package simulator

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory



object SimulatorServer extends App {

  implicit val system = ActorSystem("simulator")
  implicit val ec = system.dispatcher
  implicit val mat: Materializer = ActorMaterializer()

  val log = LoggerFactory.getLogger(this.getClass)
  val conf = ConfigFactory.load()
  lazy val host = conf.getString("simulator.api.host")
  lazy val port = conf.getInt("simulator.api.port")

  val route =  path("random") {
    (post & entity(as[String])) { data =>
      println("Data received in call = "+ data)
      complete("Success")
    }
  }

  val bindingFuture = Http().bindAndHandle(route, host, port)
  bindingFuture onComplete{
    case scala.util.Success(value) => log.info("Server started on 8080")
    case scala.util.Failure(exception) => log.error("Some thing went wrong "+exception.printStackTrace())
  }
}