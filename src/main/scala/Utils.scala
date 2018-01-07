package TwitterToEventHub

object ConfigParser{
    implicit val formats =  org.json4s.DefaultFormats
    def ParseTwitterStreamConfig(fileName: String): TwitterStreamConfig = {
        println("Using Twitter config: " + fileName)
        val s = io.Source.fromFile(fileName).mkString
        org.json4s.jackson.JsonMethods.parse(s).extract[TwitterStreamConfig]
    }
    def ParseEventhubSenderConfig(fileName: String): EventHubConfig = {
        println("Using EventHub config: " + fileName)
        val s = io.Source.fromFile(fileName).mkString
        org.json4s.jackson.JsonMethods.parse(s).extract[EventHubConfig]
    }
}
