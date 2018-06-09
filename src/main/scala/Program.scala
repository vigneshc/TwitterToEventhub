package TwitterToEventHub

object Main extends App {
	args.length match {
		case 4 => {
			val twitterConfig = ConfigParser.ParseTwitterStreamConfig(args(0))
			val targetType = args(1).toLowerCase
			val eventSender =  targetType match {
				case "eventhub" => new EventhubSender(ConfigParser.ParseEventhubSenderConfig(args(2)), new RoundRobinPartitioner(2))
				case "console" => new ConsoleSender()
				case "gziptextfile" => new JsonFileSender(args(2), true)
				case "textfile" => new JsonFileSender(args(2), false)
				case _ => throw new IllegalArgumentException(targetType)
			}
			
			val eventGenerator = new EventGenerator(
				new TwitterStream(twitterConfig, 1000),
				eventSender)

			eventGenerator.run(args(3).toInt)
		}

		case _ => this.Help()
	}

	private def Help(): Unit = {
		println("TwitterToEventHub <twitterConfigJsonFile> eventhub|console|gzipTextFile|textFile <eventHubConfigJsonFile>|<destFile> <secondsToRun>")
	}
}