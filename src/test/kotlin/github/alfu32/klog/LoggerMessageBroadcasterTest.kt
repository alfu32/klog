package github.alfu32.klog

import kotlin.test.Test
import kotlin.test.assertEquals
import java.io.PrintWriter

internal class LoggerMessageBroadcasterTest {

    @Test
    fun testSubscribe() {
        val lmb=LoggerMessageBroadcaster
        lmb.subscribe(listOf(LogMessageListener(
            name="general",
            printWriter=PrintWriter(System.out, true),
            filter={true},
            logMessageToString={ it.toString() }
        )))
        println(lmb.subscribers)
    }
    @Test
    fun testRun02() {
        println("test run 02")
        val expected = 42
        assertEquals(expected, 40 + 2 )

    }
}