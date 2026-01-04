package com.maxim.settings.screen.language_screen

import com.maxim.settings.model.AppLanguageUi

sealed interface LanguageScreenIntent {

    data class OnLanguageClick(val language: AppLanguageUi) : LanguageScreenIntent
}