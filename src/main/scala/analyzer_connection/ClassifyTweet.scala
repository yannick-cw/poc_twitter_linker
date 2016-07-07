package analyzer_connection

import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.unmarshalling.Unmarshal
import rest_connection.{BulkResult, ClassifyBulk}
import spray.json._
import utils.{HttpRequester, Protocols}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by 437580 on 04/07/16.
  */
trait ClassifyTweet extends HttpRequester with Protocols {

  def classify(tweets: Seq[String]): Future[BulkResult] = {

    val req = RequestBuilding.Post("/classifyBulk", entity = HttpEntity(ContentTypes.`application/json`, ClassifyBulk("bayes_multinomial", tweets.toList).toJson.compactPrint))

    val res = futureHttpResponse(req ,host = "localhost", port = 9675)
      .flatMap{ res =>
        Unmarshal(res).to[BulkResult]
      }
    res
  }

}