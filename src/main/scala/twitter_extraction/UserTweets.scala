package twitter_extraction

import com.danielasfregola.twitter4s.TwitterClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by 437580 on 04/07/16.
  */
trait UserTweets extends App {
  val client = new TwitterClient()

  def getRecentTweets(user: String): Future[Seq[String]] = {
    val timeline = client.getUserTimelineForUser(user, count = 3200)

    timeline.map { tweets =>
      tweets
//        .filter(_.user.isDefined)
//        .filter(_.user.get.screen_name == user)
        .map(_.text)
        .filter(_.nonEmpty)
    }
  }
}
