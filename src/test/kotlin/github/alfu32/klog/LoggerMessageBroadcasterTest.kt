package github.alfu32.klog

import kotlin.test.Test
import kotlin.test.assertEquals

internal class LoggerMessageBroadcasterTest {

    @Test
    fun testRun01() {
        println("test run 01")
        val expected = 42
        assertEquals(expected, 40 + 2 )

    }
    @Test
    fun testRun02() {
        println("test run 02")
        val expected = 42
        assertEquals(expected, 40 + 2 )

    }
}