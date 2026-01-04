package com.maxim.settings.screen.dark_theme_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxim.common.result.Result
import com.maxim.common.result.asResult
import com.maxim.common.util.Logger
import com.maxim.domain.use_case.get_dark_theme_config.GetDarkThemeConfigUseCase
import com.maxim.domain.use_case.set_dark_theme_config.SetDarkThemeConfigUseCase
import com.maxim.model.DarkThemeConfig
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class DarkThemeViewModel @Inject constructor(
    private val getDarkThemeConfigUseCase: GetDarkThemeConfigUseCase,
    private val setDarkThemeConfigUseCase: SetDarkThemeConfigUseCase,
    private val logger: Logger,
): ViewModel() {

    val uiState = uiState()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DarkThemeUiState()
        )

    fun onOptionClicked(config: DarkThemeConfig) {
        viewModelScope.launch {
            setDarkThemeConfigUseCase(config)
        }
    }

    private fun uiState() =
        getDarkThemeConfigUseCase()
            .asResult()
            .map { result ->
                when (result) {
                    is Result.Success -> {
                        DarkThemeUiState(
                            currentConfig = result.data,
                            loadingStatus = DarkThemeLoadingStatus.Loaded,
                        )
                    }
                    is Result.Error -> {
                        logger.e("getDarkThemeConfigUseCase failed", result.exception)
                        DarkThemeUiState(
                            currentConfig = DarkThemeConfig.SYSTEM,
                            loadingStatus = DarkThemeLoadingStatus.Loaded,
                        )
                    }
                    Result.Loading -> {
                        DarkThemeUiState(
                            currentConfig = DarkThemeConfig.SYSTEM,
                            loadingStatus = DarkThemeLoadingStatus.Loading,
                        )
                    }
                }
            }
}