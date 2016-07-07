package rest_connection

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.util.Timeout
import analyzer_connection.ClassifyTweet
import twitter_extraction.UserTweets
import utils.{HttpRequester, Protocols}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Success}

case class TwitterRequset(username: String)

case class ClassifyRequest(algorithm: String, text: String)

case class ClassifyResult(algorithm: String, rep: Double, dem: Double)

case class BulkResult(results: List[ClassifyResult])

case class ClassifyBulk(algorithm: String, texts: List[String])


trait Service extends Protocols with HttpRequester with UserTweets with ClassifyTweet {
  implicit val system: ActorSystem
  implicit val materializer: ActorMaterializer
  val logger: LoggingAdapter

  val classifyUser = path("classifyUser") {
    (post & entity(as[TwitterRequset])) { request =>

      logger.info(s"starting to classify user ${request.username}")
      implicit val timeout = Timeout(5.seconds)
      val futureTweets = getRecentTweets(request.username)
      val futureClassifyRes =
        futureTweets.flatMap(classify)
          .map { bulkRes =>
            logger.info(s"got ${bulkRes.results.size} classified tweets back")
            val seqToAvg = bulkRes.results
            val size = seqToAvg.length
            val sumRes = seqToAvg.reduce((left, right) => ClassifyResult(left.algorithm, left.rep + right.rep, left.dem + right.dem))
            ClassifyResult(sumRes.algorithm, sumRes.rep / size, sumRes.dem / size)
          }

      onComplete(futureClassifyRes) {
        case Success(classify) =>
          logger.info(s"Classified with algorithm ${classify.algorithm}, rep: ${classify.rep}, dem: ${classify.dem}")
          complete(classify)
        case Failure(ex) =>
          logger.error(ex, "something broke")
          complete(StatusCodes.BadRequest -> "something went wrong")
      }
    }
  }
}


object AkkaHttpMicroservice extends App with Service {
  implicit val system = ActorSystem("classify-system")
  implicit val materializer = ActorMaterializer()
  val logger = Logging.getLogger(system, this)

  Http().bindAndHandle(classifyUser, "0.0.0.0", 3757)
}
