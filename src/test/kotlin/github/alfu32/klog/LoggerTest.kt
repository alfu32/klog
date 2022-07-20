package github.alfu32.klog

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import java.io.File
import java.io.PrintWriter

internal class LoggerTest {

    @Test
    fun testLog() {
        val lmb=LoggerMessageBroadcaster
        var lm:LogMessage?=null
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
                logMessageToString={
                    it ->
                        lm=it 
                        it.toString()
                }
            ),
            LogMessageListener(
                name="file-csv",
                printWriter=File("log.csv").printWriter(),
                filter={true},
                logMessageToString=LogMessageListener.CSV_STRINGIFIER
            ),
            LogMessageListener(
                name="file-json",
                printWriter=File("log.json").printWriter(),
                filter={true},
                logMessageToString=LogMessageListener.JSON_STRINGIFIER
            ),
            LogMessageListener(
                name="file-xml",
                printWriter=File("log.xml").printWriter(),
                filter={true},
                logMessageToString=LogMessageListener.XML_STRINGIFIER
            ),
        ))
        // test log
        val l=Logger()
        l.log("debug","message")
        assertEquals("debug", lm?.channelName)
        assertEquals("message", lm?.message)
        assertEquals("LoggerTest.kt", lm?.fileName)
        assertEquals(53, lm?.lineNumber)
        assertNull(lm?.moduleName)
        assertNull(lm?.moduleVersion)
        assertEquals("app", lm?.classLoaderName)
        assertEquals("github.alfu32.klog.LoggerTest", lm?.className)
        assertEquals("testLog", lm?.methodName)        
    }

    @Test
    fun testRun02() {
        println("test run 02")
        val expected = 42
        assertEquals(expected, 40 + 2 )

    }
}
