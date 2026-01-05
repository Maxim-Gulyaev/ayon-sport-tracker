package com.maxim.settings.screen.language_screen

import app.cash.turbine.test
import com.maxim.domain.use_case.get_app_language.GetAppLanguageUseCase
import com.maxim.domain.use_case.set_app_language.SetAppLanguageUseCase
import com.maxim.model.AppLanguage
import com.maxim.settings.model.AppLanguageUi
import com.maxim.settings.model.toDomain
import com.maxim.settings.model.toUi
import com.maxim.testing.rules.MainDispatcherRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
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
    fun `uiState emits Loading initially`() = runTest {
        every { getAppLanguageUseCase() } returns emptyFlow()

        viewModel = LanguageViewModel(
            getAppLanguageUseCase,
            setAppLanguageUseCase
        )

        viewModel.uiState.test {
            val initial = awaitItem()

            assertEquals(LanguageScreenState.Loading, initial.screenState)
            assertEquals(AppLanguageUi.SYSTEM, initial.appLanguage)

            expectNoEvents()
        }
    }

    @Test
    fun `uiState emits Loaded when language is emitted`() = runTest {
        val domainLang = AppLanguage.ENGLISH
        every { getAppLanguageUseCase() } returns flowOf(domainLang)

        viewModel = LanguageViewModel(
            getAppLanguageUseCase,
            setAppLanguageUseCase
        )

        viewModel.uiState.test {
            val initial = awaitItem()
            assertEquals(LanguageScreenState.Loading, initial.screenState)

            val loaded = awaitItem()
            assertEquals(LanguageScreenState.Loaded, loaded.screenState)
            assertEquals(domainLang.toUi(), loaded.appLanguage)

            expectNoEvents()
        }
    }

    @Test
    fun `uiState does not emit duplicate states for same language`() = runTest {
        val lang = AppLanguage.RUSSIAN

        every { getAppLanguageUseCase() } returns flow {
            emit(lang)
            emit(lang)
        }

        viewModel = LanguageViewModel(
            getAppLanguageUseCase,
            setAppLanguageUseCase
        )

        viewModel.uiState.test {
            awaitItem()
            awaitItem()

            expectNoEvents()
        }
    }

    @Test
    fun `onLanguageClick calls setAppLanguageUseCase`() = runTest {
        every { getAppLanguageUseCase() } returns emptyFlow()
        coEvery { setAppLanguageUseCase(any()) } just Runs

        viewModel = LanguageViewModel(
            getAppLanguageUseCase,
            setAppLanguageUseCase
        )

        val uiLang = AppLanguageUi.ENGLISH

        viewModel.onLanguageClick(uiLang)

        advanceUntilIdle()

        coVerify(exactly = 1) {
            setAppLanguageUseCase(uiLang.toDomain())
        }
    }



}