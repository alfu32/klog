# klog Kotlin Logging Framework

logging framework written in Kotlin.

## architecture

It has a reactive architecture.

The developer should use an instance of Logger
inside each class/ function.

`val logger=Logger.getInstance()`
The method logger.log(channelName, message) will place a message tagged with the channelName in the message broadcaster queue. The message broadcaster will react notifying the LoggerMessageListeners.

### Logger
#### `log(channelName: String, message: Serializable)`

Emits a StackTraceItem containing the message object on the channel
### LoggerMessageBroadcaster
#### constructor

`val lmb = LoggerMessageBroadcaster(listeners: List<LoggerEventListener>)`

The lmb should have a single instance and be configured once.

### LoggerEventListener
#### constructor

`val lel = LoggerEventListener(
  printStream:PrintStream,
  filter:(i:StackTraceItem)->Boolean,
  toString: (i:StackTraceItem)->String,
)`
It receives StackTraceItems from the Broadcaster.
If for a given message, the filter evaluates to true then it will print the string result of the toString function to the given print stream.

Anything with a file descriptor can be opened as a print stream ( sysout, files, pipes )
#### predefined static stringifiers
##### LoggerEventListener.toCsvString(i:StackTraceItem)->String

##### LoggerEventListener.toJsonString(i:StackTraceItem)->String

##### LoggerEventListener.toXmlString(i:StackTraceItem)->String


# installation

use  the script install.sh to clone and init a copy of this repository

`curl https://raw.githubusercontent.com/alfu32/starter-kotlin-maven/main/install.sh | sh -`
