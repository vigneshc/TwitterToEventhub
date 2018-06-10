package TwitterToEventHub

import com.microsoft.azure.eventhubs._;
import java.util.concurrent._;

case class EventhubCreds(namespace: String, eventhubName: String, sasKeyName: String, sasKey: String){
}

case class EventHubConfig(creds: EventhubCreds, maxBatchWaitTimeMs: Int){
}

trait EventPartitioner[TEtype]{
    def GetPartitionKey(e: TEtype): String
}

class RoundRobinPartitioner[TEtype](partitionCount: Int) extends EventPartitioner[TEtype]{
    var partitionId = 0
    def GetPartitionKey(e: TEtype): String = {
        this.partitionId = ( this.partitionId + 1 ) % this.partitionCount
        return this.partitionId.toString()
    }
}

class EventhubSender(config: EventHubConfig, partitioner: EventPartitioner[String]) extends EventSender[String]{
     val maxEventSizeBytes = 256 * 1024  * 80 / 100
     val eventBatcher: EventBatcher[String, Array[Byte]] = new ByteArrayBatcher(maxEventSizeBytes, config.maxBatchWaitTimeMs)
     val executorService = Executors.newSingleThreadExecutor();
     val ehClient: EventHubClient = EventHubClient.createSync(
         new ConnectionStringBuilder()
         .setNamespaceName(config.creds.namespace)
         .setEventHubName(config.creds.eventhubName)
         .setSasKeyName(config.creds.sasKeyName)
         .setSasKey(config.creds.sasKey)
         .toString,
         executorService);
         
     var eventCount:Long = 0

    override def Send(e: String): Unit = this.SendToEventhub(this.eventBatcher.Write(e))

    override def Flush(): Unit = this.SendToEventhub(this.eventBatcher.Flush())

    override def RemainingTimeInBatchMs(): Long = this.eventBatcher.RemainingTimeInBatchMs()

    override def Close(): Unit = {
        this.Flush()
        println("Sent :" + this.eventCount)
        this.ehClient.close()
        executorService.shutdown()
        this.eventBatcher.Close()
    }

    private def SendToEventhub(d: Option[Array[Byte]]): Unit = {
        d match {
            case Some(data) => {
                // TODO:- send async
                this.ehClient.sendSync(EventData.create(data))
                this.eventCount = this.eventCount + 1
                if(this.eventCount % 100 == 1)
                {
                    println("Sent :" + this.eventCount)
                }
            }

            case None => Unit
        }
    }
}