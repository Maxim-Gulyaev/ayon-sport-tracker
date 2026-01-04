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
import androidx.compose.ui.unit.dp
import com.maxim.model.DarkThemeConfig
import com.maxim.settings.R
import com.maxim.settings.screen.component.SettingsCheckableItem
import com.maxim.settings.screen.component.SettingsTopAppBar
import com.maxim.settings.utils.displayConfigNameRes

@Composable
fun DarkThemeScreen(
    viewModel: DarkThemeViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState(DarkThemeUiState())

    when (uiState.loadingStatus) {
        DarkThemeLoadingStatus.Loaded -> {
            DarkThemeScreenContent(
                uiState = uiState,
                onBackClick = onBackClick,
                opOptionClick = { config ->
                    viewModel.accept(DarkThemeScreenIntent.OnOptionClicked(config))
                },
            )
        }

        DarkThemeLoadingStatus.Loading -> {}
    }
}

@Composable
private fun DarkThemeScreenContent(
    uiState: DarkThemeUiState,
    onBackClick: () -> Unit,
    opOptionClick: (DarkThemeConfig) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
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
                ) { item ->
                    SettingsCheckableItem(
                        displayNameRes = item.displayConfigNameRes(),
                        isSelected = currentConfig == item,
                        onClick = { opOptionClick(item) }
                    )
                }
            }
        }
    }
}
