package com.maxim.settings.screen.language_screen

import com.maxim.settings.model.AppLanguageUi
import com.maxim.settings.utils.appLanguages
import kotlinx.collections.immutable.ImmutableList

data class LanguageUiState(
    val appLanguages: ImmutableList<AppLanguageUi> = appLanguages(),
    val appLanguage: AppLanguageUi = AppLanguageUi.SYSTEM,
    val screenState: LanguageScreenState = LanguageScreenState.Loading,
)

sealed interface LanguageScreenState {
    data object Loaded : LanguageScreenState
    data object Loading : LanguageScreenState
}