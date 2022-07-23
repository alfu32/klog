package github.alfu32.klog

import java.io.PrintWriter
import  java.io.File

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
              val p = s.printWriter()
              p.println(msg)
              p.flush()
              p.close()
            }catch(x: Exception){
              //DO NOTHING
              System.out.println("error closing printWriter ${s.name}")
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

class PrintWriterFactory(
  val printWriterFn: ()->PrintWriter,
){

}

class LogMessageListener(
  val name: String,
  val printWriter: ()->PrintWriter,
  val filter: (LogMessage)->Boolean,
  val logMessageToString: (LogMessage)->String,
){
  ////// public fun finalse(){
  //////   try{
  //////     System.out.println(
  //////       """finalising $name print writer"""
  //////     )
  //////     printWriter.flush()
  //////     printWriter.close()
  //////     System.out.println(
  //////       """finalised $name print writer"""
  //////     )        
  //////   }catch(x:Exception){
  //////     System.out.println(
  //////       """Error $x finalising $name print writer"""
  //////     )
  //////     x.printStackTrace(System.out)
  //////   }
  ////// }

  companion object {

    fun journaledFile(
      name: String,
      filenameFn: ()->String,
      logMessageToStringFn: (LogMessage)->String,
      filterFn: (LogMessage)->Boolean,
    ):LogMessageListener{
      return LogMessageListener(
        name=name,
        printWriter={File(filenameFn()).printWriter()},
        filter=filterFn,
        logMessageToString=logMessageToStringFn,
      )
    }

    val CSV_STRINGIFIER: (LogMessage)->String = {
      lm:LogMessage ->
      """
      ${
        lm.timestamp
      },${
        lm.channelName
      },${
        lm.fileName
      },${
        lm.lineNumber
      },${
        lm.moduleName
      },${
        lm.moduleVersion
      },${
        lm.classLoaderName
      },${
        lm.className
      },${
        lm.methodName
      },"${
        lm.message.toString()
        .replace(Regex("\n"),"\\n")
        .replace(Regex("\r"),"\\r")
        .replace(Regex("\""),"\\\"")
      }",
      """.trimIndent()
    }
    val JSON_STRINGIFIER: (LogMessage)->String = {
      lm:LogMessage ->
      """
      {
        "timestamp":${lm.timestamp},
        "channelName":"${lm.channelName}",
        "fileName":"${lm.fileName}",
        "lineNumber":${lm.lineNumber},
        "moduleName":"${lm.moduleName}",
        "moduleVersion":"${lm.moduleVersion}",
        "classLoaderName":"${lm.classLoaderName}",
        "className":"${lm.className}",
        "methodName":"${lm.methodName}",
        "message":"${
          lm.message.toString()
            .replace(Regex("\n"),"\\n")
            .replace(Regex("\r"),"\\r")
            .replace(Regex("\""),"\\\"")
        }"
      }
      """.trimIndent()
    }
    val XML_STRINGIFIER: (LogMessage)->String = {
      lm:LogMessage ->
      """
      <log>
        <timestamp>${lm.timestamp}</timestamp>
        <channelName>${lm.channelName}</channelName>
        <fileName>${lm.fileName}</fileName>
        <lineNumber>${lm.lineNumber}</lineNumber>
        <moduleName>${lm.moduleName}</moduleName>
        <moduleVersion>${lm.moduleVersion}</moduleVersion>
        <classLoaderName>${lm.classLoaderName}</classLoaderName>
        <className>${lm.className}</className>
        <methodName>${lm.methodName}</methodName>
        <message>${
          lm.message.toString()
            .replace(Regex("<"),"&lt;")
            .replace(Regex(">"),"&gt;")
            .replace(Regex("\""),"\\\"")
        }</message>
      </log>
      """.trimIndent()
    }
  }
  override fun toString():String{
    return name;
  }
}

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
    var place: StackTraceElement = p.stackTrace[1]
    if(place.className=="github.alfu32.klog.Logger"){
      place=p.stackTrace[2]
    }
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
