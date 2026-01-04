package com.maxim.settings.screen.language_screen

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxim.domain.use_case.get_app_language.GetAppLanguageUseCase
import com.maxim.domain.use_case.set_app_language.SetAppLanguageUseCase
import com.maxim.settings.model.AppLanguageUi
import com.maxim.settings.model.toDomain
import com.maxim.settings.model.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class LanguageViewModel @Inject constructor(
    private val getAppLanguageUseCase: GetAppLanguageUseCase,
    private val setAppLanguageUseCase: SetAppLanguageUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LanguageUiState())
    val uiState: StateFlow<LanguageUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeAppLanguage()
        }
    }

    fun onLanguageClick(language: AppLanguageUi) {
        viewModelScope.launch {
            setAppLanguageUseCase(language.toDomain())
        }
    }

    private suspend fun observeAppLanguage() {
        getAppLanguageUseCase()
            .distinctUntilChanged()
            .collect { data ->
                val languageUi = data.toUi()
                _uiState.update {
                    it.copy(
                        currentAppLanguage = languageUi,
                        selectedLanguage = languageUi,
                        screenState = LanguageScreenState.Loaded,
                    )
                }
            }
    }

    @VisibleForTesting
    fun setUiStateForTest(state: LanguageUiState) {
        _uiState.value = state
    }
}