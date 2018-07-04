name := "Scrapping Actor"

version := "0.1"

scalaVersion := "2.12.6"

mainClass := Some("MainProcess")


libraryDependencies ++= {
  Seq(
    // Biblioteka typesafe actor dla Scali
    "com.typesafe.akka" %% "akka-actor" % "2.5.13",
    "com.typesafe.akka" %% "akka-testkit" % "2.5.13" % Test
  )
}