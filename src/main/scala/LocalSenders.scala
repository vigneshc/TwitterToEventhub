package TwitterToEventHub

class ConsoleSender() extends EventSender[String]{
    override def Send(e: String): Unit = {
        println(e)
    }

    override def Flush(): Unit = { }

    override def RemainingTimeInBatchMs(): Long = 2000

    override def Close(): Unit = { }
}

class FileSender(fileName: String) extends EventSender[String]{
    println("Writing to file: " + fileName)
    val writer = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(fileName)))
    writer.write("[", 0, 1)
    var count = 0
    override def Send(e: String): Unit = {
        if(this.count > 0)
        {
            writer.write(",", 0, 1)
        }

        writer.write(e, 0, e.length)
        this.count = this.count + 1
    }

    override def Flush(): Unit = { 
        writer.flush()
    }

    override def RemainingTimeInBatchMs(): Long = 2000

    override def Close(): Unit = { 
        writer.write("]", 0, 1)
        writer.close()
        println("Wrote Records: " + this.count)
    }
}