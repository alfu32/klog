# klog Kotlin Logging Framework

logging framework written in Kotlin.

## reactive architecture

```
| logger | -> |            | -> | listener |
| logger | -> | brodcaster | -> | listener |
| logger | -> |            | -> | listener |

```

The developer should use an instance of Logger
inside each class/ function.

`val logger=Logger.getInstance()`
The method logger.log(channelName, message) will place a message tagged with the channelName in the message broadcaster queue. The message broadcaster will react notifying the LoggerMessageListeners.

### Logger
#### `log(channelName: String, message: Serializable)`

Emits a StackTraceItem containing the message object on the channel
### LoggerMessageBroadcaster
receives messages (StackTraceItems) from loggers and dispatches them to the LoggingEventListeners
Should be configured at the application entry point.
The configuration is done programmatically using the constructor.
If you need a text file based configuration you should provide yourself a solution for parsing the text file configuration into a list of LoggingEventListener.
Text based configurations are used to make the application admin-friendly.
If you are the dev and the administrator there's no point on using text based config.
#### constructor

`val lmb = LoggerMessageBroadcaster(listeners: List<LoggerEventListener>)`

The lmb should have a single instance and be configured once.

### LoggerEventListener
#### constructor

```
val lel = LoggerEventListener(
  printStream:PrintStream,
  filter:(i:StackTraceItem)->Boolean,
  toString: (i:StackTraceItem)->String,
)
```
receives StackTraceItems from the Broadcaster.
If for a given message, the filter evaluates to true then it will print the string result of the toString function to the given print stream.

Anything with a file descriptor can be opened as a print stream ( sysout, files, pipes )
#### predefined static stringifiers
##### LoggerEventListener.toCsvString(i:StackTraceItem)->String

##### LoggerEventListener.toJsonString(i:StackTraceItem)->String

##### LoggerEventListener.toXmlString(i:StackTraceItem)->String


# installation

use  the script install.sh to clone and init a copy of this repository

`curl https://raw.githubusercontent.com/alfu32/starter-kotlin-maven/main/install.sh | sh -`
