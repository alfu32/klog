package github.alfu32.klog

import java.io.PrintWriter
interface Stringifiable{
    override fun toString(): String
}
class Logger{
    public fun log(channelName: String, message: Stringifiable){

    }
}

class LoggerMessageBroadcaster(
    
){
    companion object{
        var subscribers = arrayListOf<LogMessageListener>()

    }
}

class LogMessageListener(
    val printWriter: PrintWriter,
    val filter: (LogMessage)->Boolean,
    val logMessageToString: (LogMessage)->String,
)

class LogMessage{
    val place: StackTraceElement
    init{
        val p = Throwable()
        place=p.getStackTrace()[1]
    }
}