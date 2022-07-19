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
    var subscribers = listOf<LogMessageListener>()
    fun emit(lm:LogMessage){
      subscribers.forEach{
        s -> 
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

    fun subscribe(ss: List<LogMessageListener>) {
        ss.forEach{
            s -> 
            subscribers = subscribers.filter{ other -> other.name != s.name }
        }
        subscribers=subscribers + ss
    }
  }
}

class LogMessageListener(
  val name: String,
  val printWriter: PrintWriter,
  val filter: (LogMessage)->Boolean,
  val logMessageToString: (LogMessage)->String,
)

class LogMessage(
  val channelName: String,
  val message:Any,
){
  var timestamp: Long
  var fileName: String
  var lineNumber: Int
  var moduleName: String?
  var moduleVersion: String?
  var classLoaderName: String
  var className: String
  var methodName: String
  init{
    timestamp = System.currentTimeMillis()
    val p = Throwable()
    val place: StackTraceElement = p.stackTrace[1]
    fileName = place.fileName
    lineNumber = place.lineNumber
    moduleName = place.moduleName
    moduleVersion = place.moduleVersion
    classLoaderName = place.classLoaderName
    className = place.className
    methodName = place.methodName
  }
  override fun toString():String{
    return """
      LogMessage{
        timestamp: $timestamp,
        channelName: $channelName,
        message: $message,
        fileName: $fileName,
        lineNumber: $lineNumber,
        moduleName: $moduleName,
        moduleVersion: $moduleVersion,
        classLoaderName: $classLoaderName,
        className: $className,
        methodName: $methodName,
      }
    """.trimIndent()
  }
}