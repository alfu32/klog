package github.alfu32.klog

import kotlin.test.Test
import kotlin.test.assertEquals
import java.io.PrintWriter
import java.io.File
import java.io.FileOutputStream

internal class LoggerMessageBroadcasterTest {

    @Test
    fun testSubscribe() {
        val lmb=LoggerMessageBroadcaster
        lmb.subscribe(listOf(
            LogMessageListener(
                name="console",
                printWriter={PrintWriter(System.out, true)},
                filter={true},
                logMessageToString={ it.toString() }
            ),
            LogMessageListener(
                name="file",
                printWriter={PrintWriter(FileOutputStream(File("log"),true),true)},
                filter={true},
                logMessageToString={ it.toString() }
            ),
            LogMessageListener.journaledFile(
              name="factory-log",
              filenameFn= {"factory-log.csv"},
              logMessageToStringFn={it.toString()},
              filterFn= {true},
            ),
        ))
        println(lmb.subscribers)
    }
}
