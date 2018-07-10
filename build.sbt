name := "Scrapping Actor"

version := "0.1"

scalaVersion := "2.12.6"

mainClass := Some("MainProcess")

resolvers ++= Seq(
  "Typesafe" at "http://repo.typesafe.com/typesafe/releases",
  "spray repo" at "http://repo.spray.io"
)

libraryDependencies ++= {
  Seq(
    // Biblioteka typesafe actor dla Scali
    "com.typesafe.akka" %% "akka-actor" % "2.5.13",
    // Biblioteka do scrapowania zawrtości z stron HTML
    "net.ruippeixotog" %% "scala-scraper" % "2.1.0",
    // Biblioteka typesafe Play-Framework do obsługi JSON
    "com.typesafe.play" %% "play-json" % "2.6.7",
    // Biblioteka typesafe do tworzenia konfiguracji
    "com.typesafe" % "config" % "1.3.2",
    // Testy do akka actor
    "com.typesafe.akka" %% "akka-testkit" % "2.5.13" % Test,
    "org.scalatest" %% "scalatest" % "3.0.5" % "test"
  )
}