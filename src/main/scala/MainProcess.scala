import java.net.URL
import akka.actor.{ActorSystem, PoisonPill, Props}
import scala.concurrent.duration._
import scala.concurrent.Await
import scala.io.StdIn

/**
  * Główny proces
  */
object MainProcess extends App {
  val system = ActorSystem()
  val supervisor = system.actorOf(Props[Supervisor], "supervisor")
  val url = new URL("http://bash.org.pl/latest/")
  println("Welcome in Scrapper Actor!!! Please specify how many pages do you wanna to scrap: ")
  val numberOfPages = StdIn.readInt()

  supervisor ! Start(url, numberOfPages)

  Await.result(system.whenTerminated, 10 minutes)

  supervisor ! PoisonPill
  system.terminate
}