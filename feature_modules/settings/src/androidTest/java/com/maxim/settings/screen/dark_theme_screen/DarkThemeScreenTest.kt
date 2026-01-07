package com.maxim.settings.screen.dark_theme_screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.maxim.model.DarkThemeConfig
import com.maxim.testing.test_tag.CommonTestTag
import com.maxim.testing.test_tag.SettingsTestTag
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DarkThemeScreenTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun darkThemeScreen_loading_status_should_show_loading_spinner() {
        rule.setContent {
            DarkThemeScreenContent(
                uiState = DarkThemeUiState(),
                onBackClick = {},
                onOptionClick = {},
            )
        }

        rule
            .onNodeWithTag(CommonTestTag.LOADING_SCREEN)
            .assertExists()
    }

    @Test
    fun darkThemeScreen_loading_status_content_should_not_be_visible() {
        rule.setContent {
            DarkThemeScreenContent(
                uiState = DarkThemeUiState(),
                onBackClick = {},
                onOptionClick = {},
            )
        }

        rule
            .onNodeWithTag(SettingsTestTag.DARK_THEME_SCREEN_CONTENT)
            .assertDoesNotExist()
    }

    @Test
    fun darkThemeScreen_loaded_status_should_show_all_configs() {
        val uiState = DarkThemeUiState()
            .copy(loadingStatus = DarkThemeLoadingStatus.Loaded)

        rule.setContent {
            DarkThemeScreenContent(
                uiState = uiState,
                onBackClick = {},
                onOptionClick = {},
            )
        }

        rule
            .onNodeWithTag(SettingsTestTag.DARK_THEME_SCREEN_CONTENT)
            .assertIsDisplayed()

        DarkThemeConfig.entries.forEach { config ->
            rule
                .onNodeWithTag(SettingsTestTag.SETTINGS_CHECKABLE_ITEM + config.name)
                .assertExists()
                .assertIsDisplayed()
        }
    }

    @Test
    fun darkThemeScreen_onOptionClick_should_call_viewmodel() {
        val uiState = DarkThemeUiState()
            .copy(loadingStatus = DarkThemeLoadingStatus.Loaded)
        var onOptionClickCalled = false
        val targetConfig = DarkThemeConfig.LIGHT

        rule.setContent {
            DarkThemeScreenContent(
                uiState = uiState,
                onBackClick = {},
                onOptionClick = { config ->
                    assertEquals(DarkThemeConfig.LIGHT, config)
                    onOptionClickCalled = true
                },
            )
        }

        rule
            .onNodeWithTag(SettingsTestTag.SETTINGS_CHECKABLE_ITEM + targetConfig.name)
            .performClick()

        assertEquals(true, onOptionClickCalled)
    }

    @Test
    fun darkThemeScreen_currentOption_should_be_selected() {
        val selected = DarkThemeConfig.LIGHT

        val uiState = DarkThemeUiState()
            .copy(
                loadingStatus = DarkThemeLoadingStatus.Loaded,
                currentConfig = selected,
            )

        rule.setContent {
            DarkThemeScreenContent(
                uiState = uiState,
                onBackClick = {},
                onOptionClick = {},
            )
        }

        rule
            .onNodeWithTag(SettingsTestTag.SETTINGS_CHECKABLE_ITEM + selected.name)
            .performClick()
            .assertIsSelected()
    }

    @Test
    fun darkThemeScreen_backClick_should_call_callback() {
        var isCallbackCalled = false
        val uiState = DarkThemeUiState()
            .copy(loadingStatus = DarkThemeLoadingStatus.Loaded)

        rule.setContent {
            DarkThemeScreenContent(
                uiState = uiState,
                onBackClick = {
                    isCallbackCalled = true
                },
                onOptionClick = {},
            )
        }

        rule
            .onNodeWithTag(SettingsTestTag.BACK_BUTTON)
            .performClick()

        assertEquals(true, isCallbackCalled)
    }
}