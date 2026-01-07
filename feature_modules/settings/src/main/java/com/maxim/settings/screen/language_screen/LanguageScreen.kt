package com.maxim.settings.screen.language_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maxim.settings.R
import com.maxim.settings.model.AppLanguageUi
import com.maxim.settings.screen.component.SettingsCheckableItem
import com.maxim.settings.screen.component.SettingsTopAppBar
import com.maxim.settings.utils.displayLangNameRes
import com.maxim.testing.test_tags.SettingsTestTag
import com.maxim.ui.component.AyonVerticalSpacer
import com.maxim.ui.component.LoadingScreen
import com.maxim.ui.theme.AyonTheme
import com.maxim.ui.util.AdaptivePreviewDark
import com.maxim.ui.util.AdaptivePreviewLight

@Composable
internal fun LanguageScreen(
    viewModel: LanguageViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LanguageScreenContent(
        uiState = uiState,
        onLanguageItemClick = { lang -> viewModel.onLanguageClick(lang) },
        onBackClick = onBackClick,
    )
}

@Composable
internal fun LanguageScreenContent(
    uiState: LanguageUiState,
    onLanguageItemClick: (AppLanguageUi) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState.screenState) {

        LanguageScreenState.Loaded -> {
            Scaffold(
                modifier = Modifier.testTag(SettingsTestTag.LANGUAGE_SCREEN_CONTENT),
                topBar = {
                    SettingsTopAppBar(
                        titleRes = R.string.language,
                        onBackClick = onBackClick,
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                        )
                    )
                },
            ) { paddingValues ->
                Column(
                    modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    LazyColumn(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 20.dp),
                    ) {
                        with (uiState) {
                            itemsIndexed(
                                items = appLanguages,
                                key = { _, item -> item.ordinal }
                            ) { index, item ->
                                SettingsCheckableItem(
                                    displayNameRes = item.displayLangNameRes(),
                                    isSelected = appLanguage == item,
                                    testTag = SettingsTestTag.SETTINGS_CHECKABLE_ITEM + item.name,
                                    onClick = { onLanguageItemClick(item) }
                                )
                                if (index < appLanguages.lastIndex) {
                                    AyonVerticalSpacer(8.dp)
                                }
                            }
                        }
                    }
                }
            }
        }

        LanguageScreenState.Loading -> {
            LoadingScreen()
        }
    }

}

@AdaptivePreviewDark
@Preview
@Composable
private fun PreviewLanguageScreenDark() {
    AyonTheme() {
        LanguageScreenContent(
            uiState = LanguageUiState(),
            onLanguageItemClick = {},
            onBackClick = {},
        )
    }
}

@AdaptivePreviewLight
@Preview
@Composable
private fun PreviewLanguageScreenLight() {
    AyonTheme {
        LanguageScreenContent(
            uiState = LanguageUiState(),
            onLanguageItemClick = {},
            onBackClick = {},
        )
    }
}