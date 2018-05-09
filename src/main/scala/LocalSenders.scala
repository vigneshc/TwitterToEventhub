package TwitterToEventHub

class ConsoleSender() extends EventSender[String]{
    override def Send(e: String): Unit = {
        println(e)
    }

    override def Flush(): Unit = { }

    override def RemainingTimeInBatchMs(): Long = 2000

    override def Close(): Unit = { }
}

class JsonFileSender(fileName: String) extends EventSender[String]{
    val writer = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(fileName)))

    var count = 0
    override def Send(e: String): Unit = {
        writer.write(e, 0, e.length)
        writer.newLine()
        this.count = this.count + 1
    }

    override def Flush(): Unit = { 
        writer.flush()
    }

    override def RemainingTimeInBatchMs(): Long = 2000

    override def Close(): Unit = { 
        writer.close()
    }
}