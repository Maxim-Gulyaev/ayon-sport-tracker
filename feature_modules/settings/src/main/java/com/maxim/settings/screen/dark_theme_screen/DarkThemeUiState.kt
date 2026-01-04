package com.maxim.settings.screen.dark_theme_screen

import androidx.compose.runtime.Immutable
import com.maxim.model.DarkThemeConfig
import com.maxim.settings.utils.darkThemeConfigs
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class DarkThemeUiState(
    val configs: ImmutableList<DarkThemeConfig> = darkThemeConfigs(),
    val currentConfig: DarkThemeConfig = DarkThemeConfig.SYSTEM,
    val loadingStatus: DarkThemeLoadingStatus = DarkThemeLoadingStatus.Loading,
)

sealed interface DarkThemeLoadingStatus {
    data object Loading : DarkThemeLoadingStatus
    data object Loaded : DarkThemeLoadingStatus
}

