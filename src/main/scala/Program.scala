package TwitterToEventHub

object Main extends App {
	args.length match {
		case 4 => {
			val twitterConfig = ConfigParser.ParseTwitterStreamConfig(args(0))
			val eventSender = args(1).toLowerCase match {
				case "eventhub" => new EventhubSender(ConfigParser.ParseEventhubSenderConfig(args(2)), new RoundRobinPartitioner(2))
				case _ => new FileSender(args(2))
			}
			
			val eventGenerator = new EventGenerator(
				new TwitterStream(twitterConfig, 1000),
				eventSender)

			eventGenerator.run(args(3).toInt)
		}

		case _ => this.Help()
	}

	private def Help(): Unit = {
		println("TwitterToEventHub <twitterConfigJsonFile> eventhub|any <eventHubConfigJsonFile>|<destFile> <secondsToRun>")
	}
}