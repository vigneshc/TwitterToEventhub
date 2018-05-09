package TwitterToEventHub

object ConfigParser{
    implicit val formats =  org.json4s.DefaultFormats
    def ParseTwitterStreamConfig(fileName: String): TwitterStreamConfig = {
        val s = io.Source.fromFile(fileName).mkString
        org.json4s.jackson.JsonMethods.parse(s).extract[TwitterStreamConfig]
    }
    def ParseEventhubSenderConfig(fileName: String): EventHubConfig = {
        val s = io.Source.fromFile(fileName).mkString
        org.json4s.jackson.JsonMethods.parse(s).extract[EventHubConfig]
    }
}
