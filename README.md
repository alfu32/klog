### status
![build](https://github.com/alfu32/klog/actions/workflows/build.yml/badge.svg)
![test](https://github.com/alfu32/klog/actions/workflows/test.yml/badge.svg)

# kLog[keelog] - light Kotlin Logging Framework

logging framework written in Kotlin.

## reactive architecture

Flowchart
```mermaid
  flowchart LR

  A[Logger] -->|LogMessage| N[LogEventBroadcaster]
  B[Logger] -->|LogMessage| N[LogEventBroadcaster]
  C[Logger] -->|LogMessage| N[LogEventBroadcaster]
  N[LogEventBroadcaster]-->|LogMessage| X[LogEventListener]
  N[LogEventBroadcaster]-->|LogMessage| Y[LogEventListener]
  N[LogEventBroadcaster]-->|LogMessage| Z[LogEventListener]
```

Class Diagram
```mermaid
  classDiagram
  class Logger{
    LogEventBroadcaster broadcaster
    Logger Logger()
    log()
  }

  class LogEventBroadcaster{
    LogEventBroadcaster getInstance()
    LogEventBroadcaster(List<LogEventListener> subscribers)
    List<LogEventListener> subscribers
  }

  class LogEventListener{
    PrintWriter printWriter
    filter()
    itemToString()
  }
  class LogMessage{
    <StackTraceElement>
    String channelName
    Serializable message
    List<StackTraceElement> stack
  }

```


The developer should use an instance of Logger
inside each class/function.

```kotlin
val logger=Logger()
```
The logger instance uses the LogMessageBroadcast to emit LogMessages.
The log message is dispatched to each LogMessageListener.

The method logger.log(channelName, message) will build a `LogMessage` and emit it through the message broadcaster.

### Logger

```kotlin
log(channelName: String, message: Serializable)
```

Emits a `LogMessage` through the message broker.

### LoggerMessageBroadcaster

receives messages (`LogMessage`s) from loggers and dispatches them to the `LoggingEventListener`s

Should be configured at the application entry point.
The configuration is done programmatically using the constructor.

If you need a cofiguration based on text files (.properties,xml,json,ini,...etc. ) you can choose whatever solution you deem fit for the task:  parsing the text file configuration into a list of LoggingEventListener.

Note: Text based configurations are used to make the application admin-friendly. If you are the dev and the administrator at the same time there's no point on using text based config.

If you need to bind into a well known logging framework, you can do it at the subscriber ( event listener ) level.

#### LogMessageBroadcaster constructor

```kotlin
LogMessageBroadcaster.subscribe(
  listeners= listOf<LogEventListener>(
    LogEventListener(
      name="everything out to console",
      printStream=System.out.getPrintWriter(),
      filter= (i:LogMessage)->true,
      toString= (i:LogMessage)-> i.toString(),
    ),
    LogEventListener(
      name="httpErrors",
      printStream=File("logs/http-error.log").getPrintWriter(),
      filter= (i:LogMessage)-> i.className=="HttpConnection" && i.channelName=="error",
      toString= (i:LogMessage)-> i.toString(),
    ),
  )
)
```

The LogMessageBroadcaster is static.
the subscribe method will add subscribers replacing the existing ones that have the same name.

### LogEventListener
#### constructor

```kotlin
val lel = LogEventListener(
  nam="some log event listener",
  printWriter= System.out.getPrintWriter(),
  filter=(i:LogMessage)->Boolean,
  toString= (i:LogMessage)-> "${i.timestamp} ${i.channelName.uppercase()} ${i.className} ${i.methodName} ${i.filename}:${i.lineNumber} ${i.message.toString()}",
)
```
receives `LogMessage`s from the `LogEventBroadcaster`.

If for a given message, the filter evaluates to true then it will println the result of the toString function with the given `PrintWriter`.

Anything with a file descriptor can be opened as a `PrintWriter` ( sysout, files, pipes )

#### predefined static stringifiers
##### csv
```kotlin
LoggerEventListener.CSV_STRINGIFIER(i:LogMessage)->String
```

##### json
```kotlin
LoggerEventListener.JSON_STRINGIFIER(i:LogMessage)->String
```

##### xml
```kotlin
LoggerEventListener.XML_STRINGIFIER(i:LogMessage)->String
```
# interop with other logging frameworks 
# installation
// TODO
use  the script install.sh to clone and init a copy of this repository

```bash
curl https://raw.githubusercontent.com/alfu32/starter-kotlin-maven/main/install.sh | sh -
```
