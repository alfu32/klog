package github.alfu32.klog

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class LoggerTest {

    @Test
    fun testInit() {
        println("=== test LoggerTest.testInit =========================")
        val lm=LogMessage("test", "message")
        println(lm)
        assertEquals("test",lm.channelName)
        assertEquals("message",lm.message)
        assertEquals("LoggerTest.kt",lm.fileName)
        assertEquals(12,lm.lineNumber)
        assertNull(lm.moduleName)
        assertNull(lm.moduleVersion)
        assertEquals("app",lm.classLoaderName)
        assertEquals("github.alfu32.klog.LoggerTest",lm.className)
        assertEquals("testInit",lm.methodName)
        println("=== done LoggerTest.testInit -------------------------")
    }
    @Test
    fun testRun02() {
        println("test run 02")
        val expected = 42
        assertEquals(expected, 40 + 2 )

    }
}