package github.alfu32.klog

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class LogMessageTest {

    @Test
    fun testInit() {
        println("=== test LogMessageTest.testInit =========================")
        val lm = LogMessage("test", "message")
        println(lm)
        assertEquals("test", lm.channelName)
        assertEquals("message", lm.message)
        assertEquals("LogMessageTest.kt", lm.fileName)
        assertEquals(12, lm.lineNumber)
        assertNull(lm.moduleName)
        assertNull(lm.moduleVersion)
        assertEquals("app", lm.classLoaderName)
        assertEquals("github.alfu32.klog.LogMessageTest", lm.className)
        assertEquals("testInit", lm.methodName)
        println("=== done LogMessageTest.testInit -------------------------")
    }
}