package com.maxim.run.run_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maxim.run.R
import com.maxim.testing.test_tags.RunScreenTestTag
import com.maxim.ui.component.SingleClickButton
import com.maxim.ui.theme.AyonTheme
import com.maxim.ui.theme.AyonTypography
import com.maxim.ui.theme.LocalCustomColorScheme
import com.maxim.ui.util.AdaptivePreviewDark
import com.maxim.ui.util.AdaptivePreviewLight
import java.util.Locale
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

private const val FORMAT_HOUR_MIN_SEC = "%02d:%02d:%02d"
private const val FORMAT_MIN_SEC = "%02d:%02d"

@Composable
internal fun RunScreen(
    viewModel: RunViewModel,
    quitRunScreen: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    RunScreenContainer(
        uiState = uiState,
        onStartClick = {
            viewModel.accept(RunScreenIntent.OnStartClick)
        },
        onResetClick = {
            viewModel.accept(RunScreenIntent.OnResetClick)
        },
        onSaveClick = {
            viewModel.accept(RunScreenIntent.OnSaveClick)
            quitRunScreen()
        },
    )
}

@Composable
internal fun RunScreenContainer(
    uiState: RunUiState,
    onStartClick: () -> Unit,
    onResetClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = modifier.weight(0.4f))

            SaveButtonBlock(
                onSaveClick = onSaveClick,
                onResetClick = onResetClick,
                jogDuration = uiState.jogDuration,
                isElapsedTimeInitial = uiState.jogDuration == RunUiState().jogDuration,
                isStopwatchRunning = uiState.isStopwatchRunning,
            )

            Spacer(modifier = modifier.weight(0.4f))

            StopwatchBlock(uiState.jogDuration.inWholeSeconds)

            Spacer(modifier = modifier.weight(0.9f))

            StartButtonBlock(
                isStopwatchRunning = uiState.isStopwatchRunning,
                isElapsedTimeInitial = uiState.jogDuration == RunUiState().jogDuration,
                onStartClick = onStartClick,
            )

            Spacer(modifier = modifier.weight(0.3f))
        }
    }
}

@Composable
private fun StopwatchBlock(
    elapsedSeconds: Long,
    modifier: Modifier = Modifier,
) {
    val hours = elapsedSeconds / 3600
    val minutes = (elapsedSeconds % 3600) / 60
    val seconds = elapsedSeconds % 60

    val durationString = if (hours > 0) {
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

    Text(
        modifier = Modifier.testTag(RunScreenTestTag.STOPWATCH_TEXT),
        text = durationString,
        fontSize = 60.sp,
        color = MaterialTheme.colorScheme.primary,
    )
}

@Composable
private fun SaveButtonBlock(
    jogDuration: Duration,
    isStopwatchRunning: Boolean,
    isElapsedTimeInitial: Boolean,
    onSaveClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val saveButtonEnabled = remember(jogDuration, isStopwatchRunning) {
        jogDuration > Duration.ZERO && !isStopwatchRunning
    }

    Row (
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        SingleClickButton(
            modifier = Modifier
                .width(120.dp)
                .testTag(RunScreenTestTag.SAVE_BUTTON),
            onClick = onSaveClick,
            enabled = saveButtonEnabled,
        ) {
            Text(
                text = stringResource(R.string.save_run),
                style = AyonTypography.titleLarge,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Button(
            modifier = Modifier
                .width(120.dp)
                .testTag(RunScreenTestTag.RESET_BUTTON),
            enabled = !isElapsedTimeInitial,
            onClick = onResetClick,
        ) {
            Text(
                text = stringResource(R.string.reset),
                style = AyonTypography.titleLarge,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun StartButtonBlock(
    isStopwatchRunning: Boolean,
    isElapsedTimeInitial: Boolean,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalCustomColorScheme.current

    val startButtonColor = remember(isStopwatchRunning, colors) {
        if (isStopwatchRunning) colors.cautionOrange else colors.positiveGreen
    }

    val startButtonTextRes = remember(isStopwatchRunning, isElapsedTimeInitial) {
        when {
            !isElapsedTimeInitial && !isStopwatchRunning -> R.string.continue_stopwatch
            isStopwatchRunning -> R.string.stop
            else -> R.string.start
        }
    }

    Row (
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Button(
            modifier = Modifier
                .sizeIn(minWidth = 160.dp, minHeight = 160.dp)
                .testTag(RunScreenTestTag.START_BUTTON),
            onClick = onStartClick,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = startButtonColor,
            )
        ) {
            Text(
                text = stringResource(startButtonTextRes),
                style = AyonTypography.headlineLarge,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@AdaptivePreviewDark
@Preview
@Composable
private fun PreviewRunScreenDark() {
    AyonTheme() {
        RunScreenContainer(
            uiState = RunUiState(
                jogDuration = 5.seconds,
                isStopwatchRunning = false
            ),
            onStartClick = {},
            onResetClick = {},
            onSaveClick = {},
        )
    }
}

@AdaptivePreviewLight
@Preview
@Composable
private fun PreviewRunScreenLight() {
    AyonTheme {
        RunScreenContainer(
            uiState = RunUiState(
                jogDuration = 56799.seconds,
                isStopwatchRunning = false
            ),
            onStartClick = {},
            onResetClick = {},
            onSaveClick = {},
        )
    }
}