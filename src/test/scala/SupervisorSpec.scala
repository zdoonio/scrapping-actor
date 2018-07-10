import java.net.URL
import scala.concurrent.duration._
import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, MustMatchers}

class SupervisorSpec extends TestKit(ActorSystem("test-system")) with FlatSpecLike with BeforeAndAfterAll with MustMatchers {

  override def afterAll = {
    TestKit.shutdownActorSystem(system)
  }

  "Supervisor" should "starts scrapping process using TestProbe" in {
    val sender = TestProbe()
    val supervisor = system.actorOf(Props[Supervisor])
    sender.send(supervisor, Start(new URL("http://bash.org.pl/latest/"), 1))

    expectNoMessage(1.second)

  }

  "Supervisor" should "continue scrapping process using TestProbe" in {
    val sender = TestProbe()
    val supervisor = system.actorOf(Props[Supervisor])
    sender.send(supervisor, Continue(new URL("http://bash.org.pl/latest/"), 1))

    expectNoMessage(1.second)

  }

  "Supervisor" should "say that scrapping process is finished using TestProbe" in {
    val sender = TestProbe()
    val supervisor = system.actorOf(Props[Supervisor])
    sender.send(supervisor, ScrapFinished(new URL("http://bash.org.pl/latest/?page=4")))

    expectNoMessage(1.second)

  }

  "Supervisor" should "say that index process is finished and shout down using TestProbe" in {
    val sender = TestProbe()
    val supervisor = system.actorOf(Props[Supervisor])
    sender.send(supervisor, IndexFinished(new URL("http://bash.org.pl/latest/"), Content("", "", List())))

    expectNoMessage(1.second)

  }

  "Supervisor" should "say that scrapping process is failure due some reason using TestProbe" in {
    val sender = TestProbe()
    val supervisor = system.actorOf(Props[Supervisor])
    sender.send(supervisor, ScrapFailure(new URL("http://bash.org.pl/latest/"), new Throwable("Data null")))

    expectNoMessage(1.second)

  }
}
