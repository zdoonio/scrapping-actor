import java.net.URL
import akka.actor.{ActorSystem, PoisonPill, Props}
import scala.concurrent.duration._
import scala.concurrent.Await

object MainProcess extends App {

  val system = ActorSystem()
  val supervisor = system.actorOf(Props[Supervisor],"supervisor")
  val url = new URL("http://bash.org.pl")

  supervisor ! Start(url, 20)

  Await.result(system.whenTerminated, 10 minutes)

  supervisor ! PoisonPill
  system.terminate
}