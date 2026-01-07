package com.maxim.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.maxim.testing.test_tags.CommonTestTag
import com.maxim.ui.util.AdaptivePreviewDark

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(enabled = false) {}
            .testTag(CommonTestTag.LOADING_SCREEN),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@AdaptivePreviewDark
@Preview
@Composable
fun LoadingScreenPreview() {
    LoadingScreen()
}