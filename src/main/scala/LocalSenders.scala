package TwitterToEventHub

import java.util.zip.GZIPOutputStream;

class ConsoleSender() extends EventSender[String]{
    override def Send(e: String): Unit = {
        println(e)
    }

    override def Flush(): Unit = { }

    override def RemainingTimeInBatchMs(): Long = 2000

    override def Close(): Unit = { }
}

class JsonFileSender(fileName: String, gzip: Boolean) extends EventSender[String]{
    val writer = {
        val fileOutput = new java.io.FileOutputStream(fileName);
        val outputStream = gzip match {
            case true => new java.io.OutputStreamWriter(
                new GZIPOutputStream(fileOutput), "UTF-8")
            case _ => new java.io.OutputStreamWriter(fileOutput, "UTF-8")
        }

        new java.io.BufferedWriter(outputStream)
    }
    writer.write("[")
    var count = 0
    override def Send(e: String): Unit = {
        if(this.count > 0)
        {
            writer.write(",")
        }

        val eTrimmed = e.trim()
        writer.write(eTrimmed, 0, eTrimmed.length)
        this.count = this.count + 1
    }

    override def Flush(): Unit = { 
        writer.flush()
    }

    override def RemainingTimeInBatchMs(): Long = 2000

    override def Close(): Unit = { 
        writer.write("]")
        writer.close()
    }
}