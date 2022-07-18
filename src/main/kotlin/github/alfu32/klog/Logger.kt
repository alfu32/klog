package github.alfu32.klog

import java.io.PrintWriter

class Logger{
  public fun log(channelName: String, message: Any){
    val lm = LogMessage(
      channelName=channelName,
      message=message,
    )
    LoggerMessageBroadcaster.emit(lm)
  }
}

class LoggerMessageBroadcaster(
  
){
  companion object{
    var subscribers = arrayListOf<LogMessageListener>()
    fun emit(lm:LogMessage){
      subscribers.forEach{
        s -> {
          if(s.filter(lm)){
            try{
              val msg = s.logMessageToString(lm)
              s.printWriter.println(msg)
            }catch(x: Exception){
              //DO NOTHING
            }
          }
        }
      }
    }
  }
}

class LogMessageListener(
  val printWriter: PrintWriter,
  val filter: (LogMessage)->Boolean,
  val logMessageToString: (LogMessage)->String,
)

class LogMessage(
  val channelName: String,
  val message:Any,
){
  val place: StackTraceElement
  init{
    val p = Throwable()
    place=p.getStackTrace()[1]
  }
}