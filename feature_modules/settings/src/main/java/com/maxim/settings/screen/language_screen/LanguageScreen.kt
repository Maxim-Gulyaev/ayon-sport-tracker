package com.maxim.settings.screen.language_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maxim.settings.R
import com.maxim.settings.model.AppLanguageUi
import com.maxim.settings.screen.component.SettingsCheckableItem
import com.maxim.settings.screen.component.SettingsTopAppBar
import com.maxim.settings.screen.language_screen.LanguageScreenIntent.OnLanguageClick
import com.maxim.settings.screen.language_screen.LanguageScreenIntent.OnSaveButtonClick
import com.maxim.settings.utils.displayLangNameRes
import com.maxim.ui.component.AyonVerticalSpacer
import com.maxim.ui.theme.AyonTheme
import com.maxim.ui.util.AdaptivePreviewDark
import com.maxim.ui.util.AdaptivePreviewLight

@Composable
fun LanguageScreen(
    viewModel: LanguageViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState.screenState) {
        LanguageScreenState.Loaded -> {
            LanguageScreenContent(
                uiState = uiState,
                onLanguageItemClick = { viewModel.accept(OnLanguageClick(it)) },
                onSaveButtonClick = { viewModel.accept(OnSaveButtonClick) },
                onBackClick = onBackClick,
            )
        }

        LanguageScreenState.Loading -> {}
    }
}

@Composable
private fun LanguageScreenContent(
    uiState: LanguageUiState,
    onLanguageItemClick: (AppLanguageUi) -> Unit,
    onSaveButtonClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            SettingsTopAppBar(
                titleRes = R.string.language,
                onBackClick = onBackClick,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                ),
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
                            isSelected = selectedLanguage == item,
                            onClick = { onLanguageItemClick(item) }
                        )
                        if (index < appLanguages.lastIndex) {
                            AyonVerticalSpacer(8.dp)
                        }
                    }
                }
            }

            Spacer(modifier = modifier.weight(1f))

            Button(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                enabled = uiState.selectedLanguage != uiState.currentAppLanguage,
                onClick = onSaveButtonClick,
            ) {
                Text(text = stringResource(com.maxim.ui.R.string.save))
            }

            AyonVerticalSpacer(16.dp)
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
            onSaveButtonClick = {},
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
            onSaveButtonClick = {},
            onBackClick = {},
        )
    }
}