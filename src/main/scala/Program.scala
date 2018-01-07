package TwitterToEventHub

object Main extends App {
	args.length match {
		case 3 => {
			val twitterConfig = ConfigParser.ParseTwitterStreamConfig(args(0))
			val eventhubConfig = ConfigParser.ParseEventhubSenderConfig(args(1))
			val eventGenerator = new EventGenerator(
				new TwitterStream(twitterConfig, 1000),
				new EventhubSender(eventhubConfig, new RoundRobinPartitioner(2)))

			eventGenerator.run(args(2).toInt)
		}

		case _ => this.Help()
	}

	private def Help(): Unit = {
		println("TwitterToEventHub <twitterConfigJsonFile> <eventHubConfigJsonFile> <secondsToRun>")
	}
}