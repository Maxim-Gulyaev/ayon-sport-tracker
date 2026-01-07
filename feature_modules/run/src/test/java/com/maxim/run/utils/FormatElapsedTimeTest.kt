package com.maxim.run.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class FormatElapsedTimeTest {

    @Test
    fun runScreen_formatElapsedTime_should_return_correct_string_for_zero_value() {
        val duration = 0L
        val formatted = duration.formatElapsedTime()
        assertEquals("00:00", formatted)
    }

    @Test
    fun runScreen_formatElapsedTime_should_return_correct_string_for_less_minute() {
        val duration = 59L
        val formatted = duration.formatElapsedTime()
        assertEquals("00:59", formatted)
    }

    @Test
    fun runScreen_formatElapsedTime_should_return_correct_string_for_one_minute() {
        val duration = 60L
        val formatted = duration.formatElapsedTime()
        assertEquals("01:00", formatted)
    }

    @Test
    fun runScreen_formatElapsedTime_should_return_correct_string_for_less_hour() {
        val duration = 3599L
        val formatted = duration.formatElapsedTime()
        assertEquals("59:59", formatted)
    }

    @Test
    fun runScreen_formatElapsedTime_should_return_correct_string_for_one_hour() {
        val duration = 3600L
        val formatted = duration.formatElapsedTime()
        assertEquals("01:00:00", formatted)
    }
}