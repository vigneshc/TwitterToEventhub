package TwitterToEventHub

import java.io.IOException;
import com.microsoft.azure.eventhubs._;

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
     val connStr: ConnectionStringBuilder = new ConnectionStringBuilder(config.creds.namespace, config.creds.eventhubName, config.creds.sasKeyName, config.creds.sasKey);
     val ehClient: EventHubClient = EventHubClient.createFromConnectionStringSync(connStr.toString());
     var eventCount:Long = 0

    override def Send(e: String): Unit = this.SendToEventhub(this.eventBatcher.Write(e))

    override def Flush(): Unit = this.SendToEventhub(this.eventBatcher.Flush())

    override def RemainingTimeInBatchMs(): Long = this.eventBatcher.RemainingTimeInBatchMs()

    override def Close(): Unit = {
        this.ehClient.close()
        this.eventBatcher.Close()
    }

    private def SendToEventhub(d: Option[Array[Byte]]): Unit = {
        d match {
            case Some(data) => {
                // TODO:- send async
                this.ehClient.sendSync(new EventData(data))
                this.eventCount = this.eventCount + 1
                if(this.eventCount % 10 == 1)
                {
                    println("Sent :" + this.eventCount)
                }
            }

            case None => Unit
        }
    }
}