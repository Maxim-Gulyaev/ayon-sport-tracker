package com.maxim.settings.screen.language_screen

import com.maxim.domain.use_case.get_app_language.GetAppLanguageUseCase
import com.maxim.domain.use_case.set_app_language.SetAppLanguageUseCase
import com.maxim.model.AppLanguage
import com.maxim.settings.model.AppLanguageUi
import com.maxim.testing.rules.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LanguageViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: LanguageViewModel
    private lateinit var getAppLanguageUseCase: GetAppLanguageUseCase
    private lateinit var setAppLanguageUseCase: SetAppLanguageUseCase

    @Before
    fun setup() {
        getAppLanguageUseCase = mockk()
        setAppLanguageUseCase = mockk()
    }

    @Test
    fun `screenState should be Loading when viewModel starts`() {
        viewModel = LanguageViewModel(getAppLanguageUseCase, setAppLanguageUseCase)
        assertEquals(LanguageScreenState.Loading, viewModel.uiState.value.screenState)
    }

    @Test
    fun `onOptionClicked updates currentAppLanguage correctly`() = runTest {
        val fakeDataStoreFlow = MutableStateFlow(AppLanguage.SYSTEM)
        val selectedLang = AppLanguageUi.SPANISH
        coEvery { getAppLanguageUseCase() } returns fakeDataStoreFlow
        coEvery { setAppLanguageUseCase(any()) } coAnswers {
            fakeDataStoreFlow.emit(firstArg())
        }
        viewModel = LanguageViewModel(getAppLanguageUseCase, setAppLanguageUseCase)

        advanceUntilIdle()
        viewModel.onLanguageClick(selectedLang)
        advanceUntilIdle()

        assertEquals(selectedLang, viewModel.uiState.value.appLanguage)
        coVerify(exactly = 1) { setAppLanguageUseCase(any()) }
    }

    @Test
    fun `observeAppLanguage() updates state correctly`() = runTest {
        val fakeDataStoreFlow = MutableStateFlow(AppLanguage.SPANISH)
        val expectedState = LanguageUiState().copy(
            appLanguage = AppLanguageUi.SPANISH,
            screenState = LanguageScreenState.Loaded)
        coEvery { getAppLanguageUseCase() } returns fakeDataStoreFlow

        viewModel = LanguageViewModel(getAppLanguageUseCase, setAppLanguageUseCase)
        advanceUntilIdle()

        assertEquals(expectedState.appLanguage, viewModel.uiState.value.appLanguage)
        assertEquals(expectedState.screenState, viewModel.uiState.value.screenState)
    }
}