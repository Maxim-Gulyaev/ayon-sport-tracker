package com.maxim.run.run_screen

import androidx.compose.runtime.Immutable
import kotlin.time.Duration

@Immutable
data class RunUiState(
    val jogDuration: Duration = Duration.ZERO,
    val isStopwatchRunning: Boolean = false,
)
