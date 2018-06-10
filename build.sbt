name:= "TwitterToEventHub"
val twitterClientGroupId = "com.twitter"
val twitterClientArtifactId = "hbc-core"
val twitterClientRevision = "2.2.0"
val twitter4JArtifactId = "hbc-twitter4j"

libraryDependencies += twitterClientGroupId % twitterClientArtifactId % twitterClientRevision
libraryDependencies += twitterClientGroupId % twitter4JArtifactId % twitterClientRevision

val json4sJackson = "org.json4s" %% "json4s-jackson" % "3.5.0"
libraryDependencies += json4sJackson

val eventhubGroupId = "com.microsoft.azure"
val eventhubArtifactId = "azure-eventhubs"
val eventhubVersion = "1.0.1"
libraryDependencies += eventhubGroupId % eventhubArtifactId % eventhubVersion