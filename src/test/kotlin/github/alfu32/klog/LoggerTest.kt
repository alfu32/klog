package github.alfu32.klog

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import java.io.File
import java.io.PrintWriter
import java.io.FileOutputStream
import java.time.format.DateTimeFormatter
import java.time.Instant

internal class LoggerTest {

    @Test
    fun testLog() {
        val lmb=LoggerMessageBroadcaster
        var lm:LogMessage?=null
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
                logMessageToString={
                    it ->
                        lm=it 
                        it.toString()
                }
            ),
            LogMessageListener(
                name="file-csv",
                printWriter={PrintWriter(FileOutputStream(File("log.csv"),true),true)},
                filter={true},
                logMessageToString=LogMessageListener.CSV_STRINGIFIER
            ),
            LogMessageListener(
                name="file-json",
                printWriter={PrintWriter(FileOutputStream(File("log.json"),true),true)},
                filter={true},
                logMessageToString=LogMessageListener.JSON_STRINGIFIER
            ),
            LogMessageListener(
                name="file-xml",
                printWriter={PrintWriter(FileOutputStream(File("log.xml"),true),true)},
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
        assertEquals(54, lm?.lineNumber)
        assertNull(lm?.moduleName)
        assertNull(lm?.moduleVersion)
        assertEquals("app", lm?.classLoaderName)
        assertEquals("github.alfu32.klog.LoggerTest", lm?.className)
        assertEquals("testLog", lm?.methodName)        
    }
    @Test
    fun testJournaled(){
        LogMessageBroadcaster.subscribe(listOf(
            LogMessageListener(
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
                logMessageToString=LogMessageListener.JSON_STRINGIFIER,
            )
        ))
    }
}
