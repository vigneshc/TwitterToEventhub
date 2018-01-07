package TwitterToEventHub

class ConsoleSender() extends EventSender[String]{
    override def Send(e: String): Unit = {
        println(e)
    }

    override def Flush(): Unit = { }

    override def RemainingTimeInBatchMs(): Long = 2000

    override def Close(): Unit = { }
}