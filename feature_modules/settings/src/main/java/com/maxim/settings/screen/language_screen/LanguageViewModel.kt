package com.maxim.settings.screen.language_screen

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxim.common.result.Result
import com.maxim.common.result.asResult
import com.maxim.common.util.Logger
import com.maxim.domain.use_case.get_app_language.GetAppLanguageUseCase
import com.maxim.domain.use_case.set_app_language.SetAppLanguageUseCase
import com.maxim.settings.model.AppLanguageUi
import com.maxim.settings.model.toDomain
import com.maxim.settings.model.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class LanguageViewModel @Inject constructor(
    private val getAppLanguageUseCase: GetAppLanguageUseCase,
    private val setAppLanguageUseCase: SetAppLanguageUseCase,
    private val logger: Logger,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LanguageUiState())
    val uiState: StateFlow<LanguageUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeAppLanguage()
        }
    }

    fun accept(intent: LanguageScreenIntent) {
        when (intent) {
            is LanguageScreenIntent.OnLanguageClick -> {
                viewModelScope.launch {
                    setAppLanguageUseCase(intent.language.toDomain())
                }
            }
        }
    }

    private suspend fun observeAppLanguage() {
        getAppLanguageUseCase()
            .asResult()
            .collectLatest { result ->
                _uiState.update { oldState ->
                    when (result) {
                        is Result.Success -> {
                            val languageUi = result.data.toUi()
                            oldState.copy(
                                currentAppLanguage = languageUi,
                                selectedLanguage = languageUi,
                                screenState = LanguageScreenState.Loaded,
                            )
                        }

                        is Result.Error -> {
                            logger.e("setCurrentLanguage() failed", result.exception)
                            oldState.copy(
                                currentAppLanguage = AppLanguageUi.SYSTEM,
                                selectedLanguage = AppLanguageUi.SYSTEM,
                                screenState = LanguageScreenState.Loaded,
                            )
                        }

                        Result.Loading -> { oldState}
                    }
                }
            }
    }

    @VisibleForTesting
    fun setUiStateForTest(state: LanguageUiState) {
        _uiState.value = state
    }
}