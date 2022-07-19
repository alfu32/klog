package github.alfu32.klog

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import java.io.File

internal class LoggerTest {

    @ Test
    fun testSubscribe() {
        val lmb=LoggerMessageBroadcaster
        lmb.subscribe(listOf(
            LogMessageListener(
                name="console",
                printWriter=PrintWriter(System.out, true),
                filter={true},
                logMessageToString={ it.toString() }
            ),
            LogMessageListener(
                name="file",
                printWriter=File("log").printWriter(),
                filter={true},
                logMessageToString={ it.toString() }
            ),
        ))
        println(lmb.subscribers)
        val l=Logger()
        l.log("debug","message")
    }
    @Test
    fun testRun02() {
        println("test run 02")
        val expected = 42
        assertEquals(expected, 40 + 2 )

    }
}
