package com.maxim.run.run_screen

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.maxim.run.R
import com.maxim.testing.test_tags.RunScreenTestTag
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

class RunScreenTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun runScreen_initial_start_button_text_is_correct() {
        rule.setContent {
            RunScreenContainer(
                uiState = RunUiState(),
                onStartClick = {},
                onResetClick = {},
                onSaveClick = {},
            )
        }

        rule
            .onNodeWithTag(RunScreenTestTag.START_BUTTON)
            .assertTextEquals(rule.activity.getString(R.string.start))
    }

    @Test
    fun runScreen_running_start_button_text_is_correct() {
        val uiState = RunUiState()
            .copy(isStopwatchRunning = true)

        rule.setContent {
            RunScreenContainer(
                uiState = uiState,
                onStartClick = {},
                onResetClick = {},
                onSaveClick = {},
            )
        }

        rule
            .onNodeWithTag(RunScreenTestTag.START_BUTTON)
            .assertTextEquals(rule.activity.getString(R.string.stop))
    }

    @Test
    fun runScreen_paused_start_button_text_is_correct() {
        val uiState = RunUiState(
            isStopwatchRunning = false,
            jogDuration = 5.seconds,
        )

        rule.setContent {
            RunScreenContainer(
                uiState = uiState,
                onStartClick = {},
                onResetClick = {},
                onSaveClick = {},
            )
        }

        rule
            .onNodeWithTag(RunScreenTestTag.START_BUTTON)
            .assertTextEquals(rule.activity.getString(R.string.continue_stopwatch))
    }

    @Test
    fun runScreen_initial_start_button_click_should_call_onStartClick() {
        var isStartClickCalled = false

        rule.setContent {
            RunScreenContainer(
                uiState = RunUiState(),
                onStartClick = {
                    isStartClickCalled = true
                },
                onResetClick = {},
                onSaveClick = {},
            )
        }

        rule
            .onNodeWithTag(RunScreenTestTag.START_BUTTON)
            .performClick()

        assertEquals(true, isStartClickCalled)
    }

    @Test
    fun runScreen_reset_button_text_is_correct() {
        rule.setContent {
            RunScreenContainer(
                uiState = RunUiState(),
                onStartClick = {},
                onResetClick = {},
                onSaveClick = {},
            )
        }

        rule
            .onNodeWithTag(RunScreenTestTag.RESET_BUTTON)
            .assertTextEquals(rule.activity.getString(R.string.reset))
    }

    @Test
    fun runScreen_when_jogDuration_is_zero_reset_button_should_be_disabled() {
        rule.setContent {
            RunScreenContainer(
                uiState = RunUiState(),
                onStartClick = {},
                onResetClick = {},
                onSaveClick = {},
            )
        }

        rule
            .onNodeWithTag(RunScreenTestTag.RESET_BUTTON)
            .assertIsNotEnabled()
    }

    @Test
    fun runScreen_when_jogDuration_is_not_zero_reset_button_should_be_enabled() {
        rule.setContent {
            RunScreenContainer(
                uiState = RunUiState(jogDuration = 5.seconds),
                onStartClick = {},
                onResetClick = {},
                onSaveClick = {},
            )
        }

        rule
            .onNodeWithTag(RunScreenTestTag.RESET_BUTTON)
            .assertIsEnabled()
    }

    @Test
    fun runScreen_reset_button_click_should_call_onResetClick() {
        var isResetButtonClicked = false

        rule.setContent {
            RunScreenContainer(
                uiState = RunUiState(jogDuration = 5.seconds),
                onStartClick = {},
                onResetClick = {
                    isResetButtonClicked = true
                },
                onSaveClick = {},
            )
        }

        rule
            .onNodeWithTag(RunScreenTestTag.RESET_BUTTON)
            .performClick()

        assertEquals(true, isResetButtonClicked)
    }

    @Test
    fun runScreen_when_jogDuration_is_zero_save_button_should_be_disabled() {
        rule.setContent {
            RunScreenContainer(
                uiState = RunUiState(),
                onStartClick = {},
                onResetClick = {},
                onSaveClick = {},
            )
        }

        rule
            .onNodeWithTag(RunScreenTestTag.SAVE_BUTTON)
            .assertIsNotEnabled()
    }

    @Test
    fun runScreen_when_stopwatch_is_running_save_button_should_be_disabled() {
        val uiState = RunUiState(
            jogDuration = 5.seconds,
            isStopwatchRunning = true,
        )
        rule.setContent {
            RunScreenContainer(
                uiState = uiState,
                onStartClick = {},
                onResetClick = {},
                onSaveClick = {},
            )
        }

        rule
            .onNodeWithTag(RunScreenTestTag.SAVE_BUTTON)
            .assertIsNotEnabled()
    }

    @Test
    fun runScreen_when_stopwatch_is_idle_and_jogDuration_is_not_zero_save_button_should_be_enabled() {
        val uiState = RunUiState(
            jogDuration = 5.seconds,
            isStopwatchRunning = false,
        )
        rule.setContent {
            RunScreenContainer(
                uiState = uiState,
                onStartClick = {},
                onResetClick = {},
                onSaveClick = {},
            )
        }

        rule
            .onNodeWithTag(RunScreenTestTag.SAVE_BUTTON)
            .assertIsEnabled()
    }

    @Test
    fun runScreen_saveButton_click_should_call_onSaveClick() {
        var isOnSaveClickCalled = false
        val uiState = RunUiState(
            jogDuration = 5.seconds,
            isStopwatchRunning = false,
        )
        rule.setContent {
            RunScreenContainer(
                uiState = uiState,
                onStartClick = {},
                onResetClick = {},
                onSaveClick = {
                    isOnSaveClickCalled = true
                },
            )
        }

        rule
            .onNodeWithTag(RunScreenTestTag.SAVE_BUTTON)
            .performClick()

        assertEquals(true, isOnSaveClickCalled)
    }

    @Test
    fun runScreen_stopwatch_block_should_show_jogDuration() {
        val uiState = RunUiState(
            jogDuration = 5.seconds,
            isStopwatchRunning = true,
        )

        rule.setContent {
            RunScreenContainer(
                uiState = uiState,
                onStartClick = {},
                onResetClick = {},
                onSaveClick = {},
            )
        }

        rule
            .onNodeWithTag(RunScreenTestTag.STOPWATCH_TEXT)
            .assertTextEquals("00:05")
    }
}