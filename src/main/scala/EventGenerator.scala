package TwitterToEventHub

trait EventSender[TEtype]{
    // Enques event to send. Could do synchronous io every couple of events.
    def Send(e: TEtype): Unit

    // Flushes remaining events in batch.
    def Flush(): Unit

    // time remining before events in batch will be flushed.
    def RemainingTimeInBatchMs(): Long

    // closes streams.
    def Close(): Unit
}

trait EventReceiver[TEtype]{
    
    // starts the event receiver.
    def Open(): Unit

    // Polls for new events, for a maximum of durationMs milliseconds.
    def Poll(durationMs: Long): Option[TEtype]

    // closes event receiver.
    def Close(): Unit
}

// Bridges event receiver and event sender.
class EventGenerator[TEventType](eventReceiver: EventReceiver[TEventType], eventSender: EventSender[TEventType])
{
    // generates events for 'seconds'. If seconds is -1, runs forever.
    def run(seconds: Int): Unit = {
        println("Running for seconds: " + seconds)
        this.eventReceiver.Open()
        val startTime: Long = System.currentTimeMillis
        val ms = seconds * 1000
        while(seconds == -1 || System.currentTimeMillis < (startTime + ms)  )
        {
            this.eventReceiver.Poll(this.eventSender.RemainingTimeInBatchMs()) match {
                case Some(item) => this.eventSender.Send(item)
                case _ => None
            }
        }

        println("Close receiver and flushing sender")
        
        this.eventReceiver.Close()
        this.eventSender.Flush()
        this.eventSender.Close()
        
        println("Completed")
    }
}