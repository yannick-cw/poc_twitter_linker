package utils

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import rest_connection._
import spray.json.DefaultJsonProtocol

trait Protocols extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val twitterRequestFormat = jsonFormat1(TwitterRequset.apply)
  implicit val classifyResultFormat = jsonFormat3(ClassifyResult.apply)
  implicit val classifyRequestFormat = jsonFormat2(ClassifyRequest.apply)
  implicit val classifyBulkFormat = jsonFormat2(ClassifyBulk.apply)
  implicit val bulkResultFormat = jsonFormat1(BulkResult.apply)
}
