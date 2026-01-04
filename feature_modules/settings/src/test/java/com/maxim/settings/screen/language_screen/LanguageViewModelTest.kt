package com.maxim.settings.screen.language_screen

import com.maxim.common.util.NoopLog
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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okio.IOException
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
        viewModel = LanguageViewModel(getAppLanguageUseCase, setAppLanguageUseCase, NoopLog)
        assertEquals(LanguageScreenState.Loading, viewModel.uiState.value.screenState)
    }

    @Test
    fun `OnSaveButtonClick updates currentAppLanguage correctly`() = runTest {
        val fakeDataStoreFlow = MutableStateFlow(AppLanguage.SYSTEM)
        val selectedLang = AppLanguageUi.SPANISH
        coEvery { getAppLanguageUseCase() } returns fakeDataStoreFlow
        coEvery { setAppLanguageUseCase(any()) } coAnswers {
            fakeDataStoreFlow.emit(firstArg())
        }
        viewModel = LanguageViewModel(getAppLanguageUseCase, setAppLanguageUseCase, NoopLog)

        advanceUntilIdle()
        viewModel.onLanguageClick(selectedLang)
        advanceUntilIdle()

        assertEquals(selectedLang, viewModel.uiState.value.currentAppLanguage)
        coVerify(exactly = 1) { setAppLanguageUseCase(any()) }
    }

    @Test
    fun `observeAppLanguage() updates state correctly`() = runTest {
        val fakeDataStoreFlow = MutableStateFlow(AppLanguage.SPANISH)
        val expectedState = LanguageUiState().copy(
            currentAppLanguage = AppLanguageUi.SPANISH,
            selectedLanguage = AppLanguageUi.SPANISH,
            screenState = LanguageScreenState.Loaded)
        coEvery { getAppLanguageUseCase() } returns fakeDataStoreFlow

        viewModel = LanguageViewModel(getAppLanguageUseCase, setAppLanguageUseCase, NoopLog)
        advanceUntilIdle()

        assertEquals(expectedState.currentAppLanguage, viewModel.uiState.value.currentAppLanguage)
        assertEquals(expectedState.selectedLanguage, viewModel.uiState.value.selectedLanguage)
        assertEquals(expectedState.screenState, viewModel.uiState.value.screenState)
    }

    @Test
    fun `observeAppLanguage() sets fallback state when getAppLanguageUseCase throws exception`() = runTest {
        val testException = IOException("Test exception")
        val expectedState = LanguageUiState().copy(
            currentAppLanguage = AppLanguageUi.SYSTEM,
            selectedLanguage = AppLanguageUi.SYSTEM,
            screenState = LanguageScreenState.Loaded,)
        coEvery { getAppLanguageUseCase() } returns flow {
            throw testException
        }

        viewModel = LanguageViewModel(getAppLanguageUseCase, setAppLanguageUseCase, NoopLog)
        advanceUntilIdle()

        assertEquals(expectedState.currentAppLanguage, viewModel.uiState.value.currentAppLanguage)
        assertEquals(expectedState.selectedLanguage, viewModel.uiState.value.selectedLanguage)
        assertEquals(expectedState.screenState, viewModel.uiState.value.screenState)
    }
}