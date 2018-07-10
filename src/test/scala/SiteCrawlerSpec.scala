import java.net.URL
import scala.concurrent.duration._
import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, MustMatchers}

class SiteCrawlerSpec extends TestKit(ActorSystem("test-system")) with FlatSpecLike with BeforeAndAfterAll with MustMatchers {

  override def afterAll = {
    TestKit.shutdownActorSystem(system)
  }

/*  "SiteCrawler" should "give back response if scrapping process successful using TestProbe" in {
    val sender = TestProbe()
    val siteCrawler = system.actorOf(Props[SiteCrawler])
    sender.send(siteCrawler, Scrap(new URL("http://bash.org.pl/latest/?page=4")))

    val state = sender.expectMsgType[ScrapFinished]
    state must equal(ScrapFinished(new URL("http://bash.org.pl/latest/?page=4")))

  }*/
}
