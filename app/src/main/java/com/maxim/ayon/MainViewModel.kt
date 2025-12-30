package com.maxim.ayon

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxim.domain.use_case.get_app_language.GetAppLanguageUseCase
import com.maxim.domain.use_case.get_dark_theme_config.GetDarkThemeConfigUseCase
import com.maxim.model.AppLanguage
import com.maxim.model.DarkThemeConfig
import com.maxim.settings.model.AppLanguageUi
import com.maxim.settings.model.toUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getAppLanguageUseCase: GetAppLanguageUseCase,
    private val getDarkThemeConfigUseCase: GetDarkThemeConfigUseCase,
): ViewModel() {

    private val _isAppReady = MutableStateFlow(false)

    private val _theme = MutableStateFlow(DarkThemeConfig.SYSTEM)
    val theme = _theme.asStateFlow()

    init {
        observeAppState(
            languageFlow = getAppLanguageUseCase(),
            themeFlow = getDarkThemeConfigUseCase()
        )
    }

    fun isAppReady(): Boolean = _isAppReady.value

    private fun observeAppState(
        languageFlow: Flow<AppLanguage>,
        themeFlow: Flow<DarkThemeConfig>,
    ) {
        viewModelScope.launch {
            combine(
                languageFlow.distinctUntilChanged(),
                themeFlow.distinctUntilChanged()
            ) { language, theme ->
                language.toUi() to theme
            }.collect { (language, theme) ->
                applyAppState(language, theme)
            }
        }
    }

    private fun applyAppState(
        language: AppLanguageUi,
        theme: DarkThemeConfig
    ) {
        setAppLanguage(language)
        _theme.update { theme }
        _isAppReady.value = true
    }

    private fun setAppLanguage(language: AppLanguageUi) {
        val newLocale = LocaleListCompat.forLanguageTags(language.tag)
        val currentLocale = AppCompatDelegate.getApplicationLocales()

        if (currentLocale.toLanguageTags() != newLocale.toLanguageTags()) {
            AppCompatDelegate.setApplicationLocales(newLocale)
        }
    }
}