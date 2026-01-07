package com.maxim.settings.screen.dark_theme_screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.maxim.model.DarkThemeConfig
import com.maxim.settings.R
import com.maxim.settings.screen.component.SettingsCheckableItem
import com.maxim.settings.screen.component.SettingsTopAppBar
import com.maxim.settings.utils.displayConfigNameRes
import com.maxim.testing.test_tag.SettingsTestTag
import com.maxim.ui.component.LoadingScreen

@Composable
internal fun DarkThemeScreen(
    viewModel: DarkThemeViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState(DarkThemeUiState())

    DarkThemeScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onOptionClick = { config -> viewModel.onOptionClicked(config) },
    )
}

@Composable
internal fun DarkThemeScreenContent(
    uiState: DarkThemeUiState,
    onBackClick: () -> Unit,
    onOptionClick: (DarkThemeConfig) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState.loadingStatus) {

        DarkThemeLoadingStatus.Loaded -> {
            Scaffold(
                modifier = Modifier.testTag(SettingsTestTag.DARK_THEME_SCREEN_CONTENT),
                topBar = {
                    SettingsTopAppBar(
                        titleRes = R.string.dark_mode_settings,
                        onBackClick = onBackClick,
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                        ),
                    )
                }
            ) { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                ) {
                    with (uiState) {
                        items(
                            items = configs,
                            key = { it.ordinal }
                        ) { config ->
                            SettingsCheckableItem(
                                displayNameRes = config.displayConfigNameRes(),
                                isSelected = currentConfig == config,
                                testTag = SettingsTestTag.SETTINGS_CHECKABLE_ITEM + config.name,
                                onClick = { onOptionClick(config) }
                            )
                        }
                    }
                }
            }
        }

        DarkThemeLoadingStatus.Loading -> {
            LoadingScreen()
        }
    }
}
