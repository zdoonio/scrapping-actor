import java.net.URL
import scala.concurrent.duration._
import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, MustMatchers}

class ContentSaverSpec extends TestKit(ActorSystem("test-system")) with FlatSpecLike with BeforeAndAfterAll with MustMatchers {

  override def afterAll = {
    TestKit.shutdownActorSystem(system)
  }

  "ContentSaver" should "save data in file on disc" in {

    val sender = TestProbe()
    val contentSaver = system.actorOf(Props[ContentSaver])
    sender.send(contentSaver, ContentToSave(List(
      Content("", "", List(Post(1,25,"okej",1515), Post(2,25,"postNr2",1515))),
      Content("", "", List(Post(25515,-21,"<kiedys> to <bylo>",1515), Post(2,995,"post kolejny",1515)))
    )))

    expectNoMessage(1.second)
  }
}
