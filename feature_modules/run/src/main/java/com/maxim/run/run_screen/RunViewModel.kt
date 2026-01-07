package com.maxim.run.run_screen

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxim.domain.use_case.save_jog.SaveJogUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

private const val STOPWATCH_DELAY = 1_000L

class RunViewModel @Inject constructor(
    private val saveJogUseCase: SaveJogUseCase,
): ViewModel() {

    private val _uiState: MutableStateFlow<RunUiState> = MutableStateFlow(RunUiState())
    val uiState: StateFlow<RunUiState> = _uiState.asStateFlow()

    private var stopwatchJob: Job? = null

    fun accept(intent: RunScreenIntent) {
        when (intent) {
            RunScreenIntent.OnStartClick -> if (_uiState.value.isStopwatchRunning) pauseStopwatch() else startStopwatch()

            RunScreenIntent.OnResetClick -> resetStopwatch()

            RunScreenIntent.OnSaveClick -> {
                if (!_uiState.value.isStopwatchRunning) {
                    viewModelScope.launch {
                        saveJogUseCase(_uiState.value.jogDuration)
                    }
                }
            }
        }
    }

    private fun startStopwatch() {
        if (_uiState.value.isStopwatchRunning) return

        launchStopwatchJob()

        _uiState.update { it.copy(isStopwatchRunning = true) }
    }

    private fun pauseStopwatch() {
        stopwatchJob?.cancel()

        _uiState.update { it.copy(isStopwatchRunning = false,) }
    }

    private fun resetStopwatch() {
        stopwatchJob?.cancel()

        _uiState.update {
            RunUiState()
        }
    }

    private fun launchStopwatchJob() {
        stopwatchJob?.cancel()

        stopwatchJob = viewModelScope.launch {
            while (isActive) {
                delay(STOPWATCH_DELAY)
                _uiState.update { state ->
                    state.copy(jogDuration = state.jogDuration.plus(1.seconds))
                }
            }
        }
    }

    @VisibleForTesting
    fun setUiStateForTest(state: RunUiState) {
        _uiState.value = state
    }
}