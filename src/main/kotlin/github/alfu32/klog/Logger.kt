package github.alfu32.klog

class Logger(
    val lmb: LoggerMessageBroadcaster=LoggerMessageBroadcaster.getInstance()
){}

class LoggerMessageBroadcaster(
    val subscribers: List<LogMessageListener>
){
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