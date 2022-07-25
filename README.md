### status
![build](https://github.com/alfu32/klog/actions/workflows/build.yml/badge.svg)
![test](https://github.com/alfu32/klog/actions/workflows/test.yml/badge.svg)

# kLog - light Kotlin Logging Middleware

JVM logging middleware written in Kotlin.

## architecture

Flowchart
```mermaid
  flowchart LR

  A[Logger] -->|LogMessage| N[LogMessageBroadcaster]
  B[Logger] -->|LogMessage| N[LogMessageBroadcaster]
  C[Logger] -->|LogMessage| N[LogMessageBroadcaster]
  N[LogMessageBroadcaster]-->|LogMessage| X[LogMessageListener]
  N[LogMessageBroadcaster]-->|LogMessage| Y[LogMessageListener]
  N[LogMessageBroadcaster]-->|LogMessage| Z[LogMessageListener]
```

Class Diagram
```mermaid
  classDiagram
  class Logger{
    LogMessageBroadcaster broadcaster
    Logger Logger()
    log()
  }

  class LogMessageBroadcaster{
    LogMessageBroadcaster getInstance()
    LogMessageBroadcaster(List<LogMessageListener> subscribers)
    List<LogMessageListener> subscribers
  }

  class LogMessageListener{
    PrintWriter printWriter()
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

receives messages (`LogMessage`s) from loggers and dispatches them to the `LogMessageListener`s

Should be configured at the application entry point.
The configuration is done programmatically using the constructor.

If you need a cofiguration based on text files (.properties,xml,json,ini,...etc. ) you can choose whatever solution you deem fit for the task:  parsing the text file configuration into a list of LogMessageListener.

Note: 
----
_Text based configurations are used to make the application admin-friendly. If you are the dev and the administrator at the same time there's no point on using text based config._

If you need to bind into a well known logging framework, you can do it at the subscriber ( event listener ) level.

#### LogMessageBroadcaster constructor

```kotlin
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter

..........

LogMessageBroadcaster.subscribe(
  listeners= listOf<LogEventListener>(
    LogMessageListener(
      name="everything out to console",
      printWriter= {PrintWriter(System.out, true)},
      filter= {true},
      logMessageToString = {it.toString()},
    ),
    LogMessageListener(
      name="httpErrors",
      printWriter={PrintWriter(FileOutputStream(File("logs/http-error.log"),true),true)},
      filter= {it.className=="HttpConnection" && it.channelName=="error"},
      toString= (i:LogMessage)-> {it.toString()},
    ),
  )
)
```

The LogMessageBroadcaster is static.
the subscribe method will add subscribers replacing the existing ones that have the same name.

### LogMessageListener
#### constructor

```kotlin
val lel = LogMessageListener(
  name="some log event listener",
  printWriter= {PrintWriter(System.out,true)},
  filter={true},
  toString= {i -> "${i.timestamp} ${i.channelName.uppercase()} ${i.className} ${i.methodName} ${i.filename}:${i.lineNumber} ${i.message.toString()}"},
)
```
receives `LogMessage`s from the `LogMessageBroadcaster`.

If for a given message, the filter evaluates to true then it will println the result of the toString function using one instance from the LogMessageListener.printWriter() mixin.

Anything with a file descriptor can be opened as a `PrintWriter` ( sysout, files, pipes )

The factory function opens the possibility to journal the files like this

```kotlin
 val lel = LogMessageListener(
  name="journaled-json",
  printWriter={
    val dateStamp=DateTimeFormater.ofPattern("yyyy-MM-dd").format(Instant.now())
    val fn = "log-json-$dateStamp.json"
    PrintWriter(
      FileOutputStream(
        File(fn),
        true,/*open in append mode
          if false or absent it will
          rewrite the file every time
          a log will be printed*/
      ),
      true,/* autoflush */
    )
  },
  filter={true},
  toString=LogMessageListener.JSON_STRINGIFIER,
 )
```

#### predefined static stringifiers
##### csv
```kotlin
LogMessageListener.CSV_STRINGIFIER(i:LogMessage)->String
```

##### json
```kotlin
LogMessageListener.JSON_STRINGIFIER(i:LogMessage)->String
```

##### xml
```kotlin
LogMessageListener.XML_STRINGIFIER(i:LogMessage)->String
```
# interoperability with other components/systems

The message broadcaster is designed to sink all the messages to a print writer.
The usual use case is to either print to the system console or to write them to files.

If you need to propagate the message to other applications you can simply wrap a pipe in a print writer
and subscribe with it to the broadcaster.

Actually, you can wrap many things in this print writer: a SSE server, a http notification to monitoring applications.
You can equip your writer with a change detector, in which case the receiver could be notified only when change occurs.

# roadmap
[v] fully functional POC
[v] testing: coverage
[ ] nicetohave: badges (coverage, package managers )
[ ] improvement: documentation
[ ] integration: publish to maven central 
[ ] integration: publish to other artefact stores
[ ] testing: negative and edge cases
[ ] feature: parallel message dispatch
[ ] feature: more message stringifiers ( cbor, )
[ ] feature: change detector prototype

# installation
// TODO
use  the script install.sh to clone and init a copy of this repository

```bash
curl https://raw.githubusercontent.com/alfu32/starter-kotlin-maven/main/install.sh | sh -
```
