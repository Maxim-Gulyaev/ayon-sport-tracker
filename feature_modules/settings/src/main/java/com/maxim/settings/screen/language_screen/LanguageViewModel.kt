package com.maxim.settings.screen.language_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxim.domain.use_case.get_app_language.GetAppLanguageUseCase
import com.maxim.domain.use_case.set_app_language.SetAppLanguageUseCase
import com.maxim.settings.model.AppLanguageUi
import com.maxim.settings.model.toDomain
import com.maxim.settings.model.toUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class LanguageViewModel @Inject constructor(
    private val getAppLanguageUseCase: GetAppLanguageUseCase,
    private val setAppLanguageUseCase: SetAppLanguageUseCase,
) : ViewModel() {

    val uiState = uiState()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LanguageUiState()
        )

    fun onLanguageClick(language: AppLanguageUi) {
        viewModelScope.launch {
            setAppLanguageUseCase(language.toDomain())
        }
    }

    private fun uiState() =
        getAppLanguageUseCase()
            .distinctUntilChanged()
            .map { lang ->
                LanguageUiState(
                    appLanguage = lang.toUi(),
                    screenState = LanguageScreenState.Loaded,
                )
            }
}