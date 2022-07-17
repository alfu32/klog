# kLLog[killog] - light Kotlin Logging Framework

logging framework written in Kotlin.

## reactive architecture

```
many loggers         one broadcast         many listeners
   | logger | --\      object        /--> | listener |
                 \> +-------------+ /
| logger | -------> | broadcaster | -------> | listener |
               /--> +-------------+ \
   | logger |_/                     \--> | listener |

```

The developer should use an instance of Logger
inside each class/function.

```kotlin
val logger=Logger.getInstance()
```

The method logger.log(channelName, message) will place a message tagged with the channelName in the message broadcaster queue. The message broadcaster will react notifying the LoggerMessageListeners.

### Logger

```kotlin
log(channelName: String, message: Serializable)
```

Emits a StackTraceItem containing the message object on the channel

### LoggerMessageBroadcaster

receives messages (StackTraceItems) from loggers and dispatches them to the LoggingEventListeners

Should be configured at the application entry point.
The configuration is done programmatically using the constructor.

If you need a cofiguration based on text files (.properties,xml,json,ini,...etc. ) you can choose whatever solution you deem fit for the task:  parsing the text file configuration into a list of LoggingEventListener.

Note: Text based configurations are used to make the application admin-friendly. If you are the dev and the administrator at the same time there's no point on using text based config.

If you need to bind into a well known logging framework, you can do it at the subscriber ( event listener ) level.

#### LogMessageBroadcaster constructor

```kotlin
val lmb = LogMessageBroadcaster(
  listeners= listOf<LogEventListener>(
    LogEventListener(
      name="everything out to console",
      printStream=System.out.getPrintWriter(),
      filter= (i:StackTraceItem)->true,
      toString= (i:StackTraceItem)-> i.toString(),
    ),
    LogEventListener(
      name="httpErrors",
      printStream=File("logs/http-error.log").getPrintWriter(),
      filter= (i:StackTraceItem)-> i.className=="HttpConnection" && i.channelName=="error",
      toString= (i:StackTraceItem)-> i.toString(),
    ),
  )
)
```

The lmb should have a single instance and be configured once.

### LogEventListener
#### constructor

```kotlin
val lel = LogEventListener(
  nam="some log event listener",
  printWriter= System.out.getPrintWriter(),
  filter=(i:StackTraceItem)->Boolean,
  toString= (i:StackTraceItem)-> "${i.timestamp} ${i.channelName.uppercase()} ${i.className} ${i.methodName} ${i.filename}:${i.lineNumber} ${i.message.toString()}",
)
```
receives StackTraceItems from the LogEventBroadcaster.

If for a given message, the filter evaluates to true then it will println the result of the toString function with the given print writer.

Anything with a file descriptor can be opened as a print writer ( sysout, files, pipes )

#### predefined static stringifiers
##### csv
```kotlin
LoggerEventListener.toCsvString(i:StackTraceItem)->String
```

##### json
```kotlin
LoggerEventListener.toJsonString(i:StackTraceItem)->String
```

##### xml
```kotlin
LoggerEventListener.toXmlString(i:StackTraceItem)->String
```

# installation
// TODO
use  the script install.sh to clone and init a copy of this repository

```bash
curl https://raw.githubusercontent.com/alfu32/starter-kotlin-maven/main/install.sh | sh -
```
