package com.maxim.settings.screen.dark_theme_screen

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.maxim.model.DarkThemeConfig
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DarkThemeScreenTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun darkThemeScreen_loading_status_shows_loading_spinner() {
        rule.setContent {
            DarkThemeScreenContent(
                uiState = DarkThemeUiState(),
                onBackClick = {},
                onOptionClick = {},
            )
        }

        rule
            .onNodeWithTag("loadingScreen")
            .assertExists()
    }

    @Test
    fun darkThemeScreen_loading_status_content_not_visible() {
        rule.setContent {
            DarkThemeScreenContent(
                uiState = DarkThemeUiState(),
                onBackClick = {},
                onOptionClick = {},
            )
        }

        rule
            .onNodeWithTag("darkThemeScreenContent")
            .assertDoesNotExist()
    }

    @Test
    fun darkThemeScreen_loaded_status_shows_all_languages() {
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
            .onNodeWithTag("darkThemeScreenContent")
            .assertIsDisplayed()

        DarkThemeConfig.entries.forEach { config ->
            rule
                .onNodeWithTag("settingsCheckableItem" + config.name)
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
            .onNodeWithTag("settingsCheckableItem" + targetConfig.name)
            .performClick()

        assertEquals(true, onOptionClickCalled)
    }

    @Test
    fun darkThemeScreen_currentOption_isSelected() {
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
            .onNodeWithTag("settingsCheckableItem" + selected.name)
            .performClick()
            .assertIsSelected()
    }
}