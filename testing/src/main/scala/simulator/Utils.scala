package simulator

import org.slf4j.{Logger, LoggerFactory}
import simulator.ObjectMapperUtil._

trait Logging {
  implicit lazy val log: Logger = LoggerFactory.getLogger(this.getClass)
}

case class RawReport(Imei: String, Resource_path: String)

case class Reports(Imei: String, Resource_path: String)

case class CallbackEvents(Reports: List[Reports])

class ResponseException(message: String = "Unable to process Request: ",
                        cause: Throwable = None.orNull)
  extends Exception(message, cause)

object RawReport {

  def validateReport(r: RawReport): RawReport = {
    RawReport(r.Imei, r.Resource_path)
  }
}

object Reports extends Logging {
  def apply(report: RawReport): Reports = {
    val r = RawReport.validateReport(report)
    Reports(
      r.Imei,
      r.Resource_path
    )
  }
}

object CallbackEvents extends Logging {

  def apply(callBackResp: CallbackEvents): CallbackEvents = {
    CallbackEvents(callBackResp)
  }
}


case class ResponseMsg(message: Option[String] = None)

object ResponseMsg {
  def errorMsg(msg: String): ResponseMsg = ResponseMsg(Some("Error: " + msg))

  def successMsg(msg: String): ResponseMsg =
    ResponseMsg(Some("Success: " + msg))
}