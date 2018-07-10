import java.net.URL
import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, MustMatchers}

class ScraperSpec extends TestKit(ActorSystem("test-system")) with FlatSpecLike with BeforeAndAfterAll with MustMatchers {

  override def afterAll = {
    TestKit.shutdownActorSystem(system)
  }

    "Scraper" should "give back response if scrapping process successful using TestProbe" in {
    val sender = TestProbe()
    val supervisor = system.actorOf(Props[Supervisor])
    val indexer = system.actorOf(Props(new Indexer(supervisor)))
    val scraper = system.actorOf(Props(new Scraper(indexer)))
    sender.send(scraper, Scrap(new URL("http://bash.org.pl/latest/?page=4")))

    val state = sender.expectMsgType[ScrapFinished]
    state must equal(ScrapFinished(new URL("http://bash.org.pl/latest/?page=4")))

  }
}
