package com.maxim.ayon

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxim.domain.use_case.get_app_language.GetAppLanguageUseCase
import com.maxim.domain.use_case.get_dark_theme_config.GetDarkThemeConfigUseCase
import com.maxim.model.DarkThemeConfig
import com.maxim.settings.model.AppLanguageUi
import com.maxim.settings.model.toUi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getAppLanguageUseCase: GetAppLanguageUseCase,
    private val getDarkThemeConfigUseCase: GetDarkThemeConfigUseCase,
): ViewModel() {

    private val _isAppReady = MutableStateFlow(false)
    val isAppReady = _isAppReady.asStateFlow()

    fun accept(intent: InternalIntent) {
        when (intent) {
            InternalIntent.SetInitialAppState -> {
                viewModelScope.launch {
                    initAppState()
                    _isAppReady.update { true }
                    observeAppState()
                }
            }
        }
    }

    private fun setAppLanguage(language: AppLanguageUi) {
        val newLocale = LocaleListCompat.forLanguageTags(language.tag)
        val currentLocale = AppCompatDelegate.getApplicationLocales()

        if (currentLocale.toLanguageTags() != newLocale.toLanguageTags()) {
            AppCompatDelegate.setApplicationLocales(newLocale)
        }
    }

    private fun setDarkThemeConfig(config: DarkThemeConfig) {
        val nightMode = when (config) {
            DarkThemeConfig.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            DarkThemeConfig.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            DarkThemeConfig.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

    private fun observeAppState() {
        observeLanguageChanges()
        observeThemeChanges()
    }

    private suspend fun initAppState() = coroutineScope {
        val languageJob = async {
            val language = getAppLanguageUseCase().first().toUi()
            setAppLanguage(language)
        }

        val themeJob = async {
            val theme = getDarkThemeConfigUseCase().first()
            setDarkThemeConfig(theme)
        }

        languageJob.await()
        themeJob.await()
    }

    private fun observeThemeChanges() {
        viewModelScope.launch {
            getDarkThemeConfigUseCase()
                .distinctUntilChanged()
                .collect { theme ->
                    setDarkThemeConfig(theme)
                }
        }
    }

    private fun observeLanguageChanges() {
        viewModelScope.launch {
            getAppLanguageUseCase()
                .distinctUntilChanged()
                .collect { language ->
                    setAppLanguage(language.toUi())
                }
        }
    }
}

sealed interface InternalIntent {

    data object SetInitialAppState : InternalIntent
}