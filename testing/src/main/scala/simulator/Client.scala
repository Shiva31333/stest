

package simulator

import java.io.File

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader
import akka.stream.scaladsl.Source
import akka.stream.{ActorMaterializer, Materializer, ThrottleMode}
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tototoshi.csv.CSVReader
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory
import simulator.ObjectMapperUtil._

import scala.concurrent.duration._
import scala.util.Random

object Client extends App {

  val log = LoggerFactory.getLogger(this.getClass)

  val conf = ConfigFactory.load()

  implicit val system = ActorSystem("simulator")
  implicit val ec = system.dispatcher
  implicit val mat: Materializer = ActorMaterializer()

  lazy val throttleLevel = conf.getInt("simulator.event.throttle")
  lazy val maxSimulatorCount = conf.getInt("simulator.event.maxlimit")
  lazy val url = conf.getString("simulator.api.url")
  lazy val accessToken = conf.getString("simulator.api.access_token")

  val reader = CSVReader.open(new File("D:\\AWS\\kafka_redis_location_data\\src\\main\\resources\\Info.csv"))
  val elementList: List[List[String]] = reader.all()

  def random = Random.nextInt(4)

  def generateReports = {
    val randomSelectedList = elementList(random)
    Reports(randomSelectedList(0).split(":").last, randomSelectedList(1).toList.init.mkString)
  }

  def generateRandomPayload = {
    val reports = (1 to 4).toList.map(_ => generateReports)
    val event = CallbackEvents(reports)
    toJson(event)
  }


  def post(data: String) =
    Http(system).singleRequest(
      HttpRequest(
        HttpMethods.POST,
        url,
        entity = HttpEntity(ContentTypes.`application/json`, data.getBytes())
      ).withHeaders(RawHeader(accessToken, accessToken))
    )


  Source.fromIterator(() => Iterator from 0)
    .limit(maxSimulatorCount)
    .map(_ => generateRandomPayload)
    .throttle(throttleLevel, 1.second, throttleLevel, ThrottleMode.shaping)
    .runForeach {
      data => /*
        println("================")
        import com.fasterxml.jackson.module.scala.DefaultScalaModule

        val mapper = new ObjectMapper()
        mapper.registerModule(DefaultScalaModule)
        val resp = mapper.writeValueAsString(data)
        println("================================:: " + resp)*/
        log.warn("Going to call server api with data = " + data)
        post(data)
    }

}