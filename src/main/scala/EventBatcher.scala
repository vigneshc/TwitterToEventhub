package TwitterToEventHub

import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.nio.charset._;
import java.util.zip.GZIPOutputStream;

trait EventBatcher[TEType, TBatchType]{
    // Writes event to batch, may return batched events
    def Write(e: TEType): Option[TBatchType]

    // returns batched events 
    def Flush(): Option[TBatchType]

    // time remaining before events in batch will be flushed
    def RemainingTimeInBatchMs(): Long

    // closes open streams.
    def Close(): Unit
}

class ByteArrayBatcher(val maxBatchSizeBytes: Int, val maxBatchTimeMs: Long) extends EventBatcher[String, Array[Byte]]{
    val encoding = "UTF-8"
    val newLine = sys.props("line.separator").getBytes(encoding)
    val outputStream =  new ByteArrayOutputStream(maxBatchSizeBytes)
    var gzipStream = new GZIPOutputStream(outputStream)
    var lastBatchSendTimeMs: Option[Long] = None

    override def RemainingTimeInBatchMs(): Long = {
        this.lastBatchSendTimeMs match {
            case Some(l) => this.outputStream.size() match {
                case s if s > 0 => System.currentTimeMillis - l
                case _ => this.maxBatchTimeMs
            }

            case None => this.maxBatchTimeMs
        }
    }
    
    override def Write(e: String): Option[Array[Byte]] = {
        val eBytes : Array[Byte]= e.getBytes(this.encoding)
        
        return this.outputStream.size() + eBytes.length + newLine.length match {
            case s if s < this.maxBatchSizeBytes => {
                this.WriteToInternalBuffer(eBytes)
                return this.RemainingTimeInBatchMs()  match {
                    case waitTime if waitTime > this.maxBatchTimeMs => return this.Flush()
                    case _ => return None
                }
            }

            case _ => {
                val batchedData = this.Flush()
                this.WriteToInternalBuffer(eBytes)
                return batchedData
            }
        }
    }

    override def Flush(): Option[Array[Byte]] = {
        this.outputStream.size() match {
            case s if s > 0 => {
                this.lastBatchSendTimeMs = new Some(System.currentTimeMillis)
                this.gzipStream.finish()
                val output = this.outputStream.toByteArray()
                this.outputStream.reset()
                this.gzipStream = new GZIPOutputStream(this.outputStream)
                return Some(output)
            }

            case _ => None
        }
    }

    override def Close(): Unit ={
        this.gzipStream.close()
        this.outputStream.close()
    }

    private def WriteToInternalBuffer(eBytes: Array[Byte]): Unit = {
        this.gzipStream.write(eBytes, 0, eBytes.length)
        this.gzipStream.write(this.newLine, 0, this.newLine.length)
        this.lastBatchSendTimeMs match {
            case Some(e) => Unit
            case _ => this.lastBatchSendTimeMs = new Some(System.currentTimeMillis)
        }
    }
}