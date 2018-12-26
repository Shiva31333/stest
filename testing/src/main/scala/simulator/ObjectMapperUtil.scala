package simulator

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.core.{JsonGenerator, JsonParser}
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

import scala.concurrent.{ExecutionContext, Future}

object ObjectMapperUtil {

  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  mapper.setSerializationInclusion(Include.NON_ABSENT)
  mapper.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false)
  mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true)
  mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
  mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
  mapper.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true)

  mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true)
  mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
  mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true)
  mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
  mapper.configure(JsonParser.Feature.ALLOW_MISSING_VALUES, true)


  def toJson(value: Any): String =
    mapper.writeValueAsString(value)

  def toJsonMap(value: Map[Symbol, Any]): String = {
    toJson(value map { case (k,v) => k.name -> v})
  }

  def toJsonBytes(value: Any): Array[Byte] =
    toJson(value).getBytes()

  def toMap[V](json:String)(implicit m: Manifest[V]) = fromJson[Map[String,V]](json)

  def fromJson[T](json: String)(implicit x$1: Manifest[T]): T =
    mapper.readValue[T](json)

  def fromJsonBytes[T](jsonBytes: Array[Byte])(implicit x$1: Manifest[T]): T =
    mapper.readValue[T](jsonBytes)

  def fromJsonByteArrayAsync[T](json: Array[Byte])(implicit ec: ExecutionContext, m: Manifest[T]): Future[Option[T]] =
    Future {
      Option(mapper.readValue[T](json))
    }
  def fromJsonToMap(json: String): Map[String, Any] =
    if(json == null || json.isEmpty) Map[String,Any]() else mapper.readValue[Map[String,Any]](json)

  def fromJsonAsync[T](json: Array[Byte])(implicit ec: ExecutionContext, m: Manifest[T]): Future[T] =
    Future { mapper.readValue[T](json) }

  def fromJsonAsync[T](json: String)(implicit ec: ExecutionContext, m: Manifest[T]): Future[T] =
    Future { mapper.readValue[T](json) }

}