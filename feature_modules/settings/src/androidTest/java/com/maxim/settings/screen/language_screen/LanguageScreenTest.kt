package com.maxim.settings.screen.language_screen

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.maxim.settings.model.AppLanguageUi
import com.maxim.testing.test_tag.CommonTestTag
import com.maxim.testing.test_tag.SettingsTestTag
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class LanguageScreenTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun languageScreen_loading_should_show_loading_spinner() {
        rule.setContent {
            LanguageScreenContent(
                uiState = LanguageUiState(),
                onLanguageItemClick = {},
                onBackClick = {},
            )
        }

        rule
            .onNodeWithTag(CommonTestTag.LOADING_SCREEN)
            .assertExists()
    }

    @Test
    fun languageScreen_loading_content_should_not_be_visible() {
        rule.setContent {
            LanguageScreenContent(
                uiState = LanguageUiState(),
                onLanguageItemClick = {},
                onBackClick = {},
            )
        }

        rule
            .onAllNodesWithTag(SettingsTestTag.LANGUAGE_SCREEN_CONTENT)
            .assertCountEquals(0)
    }

    @Test
    fun languageScreen_loaded_should_show_all_languages() {
        val uiState = LanguageUiState()
            .copy(screenState = LanguageScreenState.Loaded)

        rule.setContent {
            LanguageScreenContent(
                uiState = uiState,
                onLanguageItemClick = {},
                onBackClick = {},
            )
        }

        rule
            .onNodeWithTag(SettingsTestTag.LANGUAGE_SCREEN_CONTENT)
            .assertExists()

        AppLanguageUi.entries.forEach { lang ->
            rule
                .onNodeWithTag(SettingsTestTag.SETTINGS_CHECKABLE_ITEM + lang.name)
                .assertExists()
        }
    }

    @Test
    fun languageScreen_current_language_should_be_checked() {
        val currentLang = AppLanguageUi.CHINESE
        val uiState = LanguageUiState()
            .copy(
                screenState = LanguageScreenState.Loaded,
                appLanguage = currentLang,
            )

        rule.setContent {
            LanguageScreenContent(
                uiState = uiState,
                onLanguageItemClick = {},
                onBackClick = {},
            )
        }

        rule
            .onNodeWithTag(SettingsTestTag.SETTINGS_CHECKABLE_ITEM + currentLang.name)
            .assertIsSelected()
    }

    @Test
    fun languageScreen_onLanguageItemClick_should_call_viewmodel() {
        var isViewModelCalled = false
        val targetLang = AppLanguageUi.CHINESE
        val uiState = LanguageUiState()
            .copy(screenState = LanguageScreenState.Loaded)

        rule.setContent {
            LanguageScreenContent(
                uiState = uiState,
                onLanguageItemClick = { lang ->
                    assertEquals(lang, targetLang)
                    isViewModelCalled = true
                },
                onBackClick = {},
            )
        }

        rule
            .onNodeWithTag(SettingsTestTag.SETTINGS_CHECKABLE_ITEM + targetLang.name)
            .performClick()

        assertEquals(true, isViewModelCalled)
    }

    @Test
    fun languageScreen_back_click_should_call_callback() {
        var isCallbackCalled = false
        val uiState = LanguageUiState()
            .copy(screenState = LanguageScreenState.Loaded)

        rule.setContent {
            LanguageScreenContent(
                uiState = uiState,
                onLanguageItemClick = {},
                onBackClick = {
                    isCallbackCalled = true
                },
            )
        }

        rule
            .onNodeWithTag(SettingsTestTag.BACK_BUTTON)
            .performClick()

        assertEquals(true, isCallbackCalled)
    }
}