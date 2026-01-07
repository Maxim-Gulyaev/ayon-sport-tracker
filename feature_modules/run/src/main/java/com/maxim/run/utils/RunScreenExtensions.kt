package com.maxim.run.utils

import java.util.Locale

private const val FORMAT_HOUR_MIN_SEC = "%02d:%02d:%02d"
private const val FORMAT_MIN_SEC = "%02d:%02d"

internal fun Long.formatElapsedTime(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60

    return if (hours > 0) {
        String.format(
            Locale.US,
            FORMAT_HOUR_MIN_SEC,
            hours,
            minutes,
            seconds
        )
    } else {
        String.format(
            Locale.US,
            FORMAT_MIN_SEC,
            minutes,
            seconds
        )
    }
}