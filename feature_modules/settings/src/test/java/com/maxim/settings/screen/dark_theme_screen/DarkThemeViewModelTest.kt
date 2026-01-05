package com.maxim.settings.screen.dark_theme_screen

import app.cash.turbine.test
import com.maxim.domain.use_case.get_dark_theme_config.GetDarkThemeConfigUseCase
import com.maxim.domain.use_case.set_dark_theme_config.SetDarkThemeConfigUseCase
import com.maxim.model.DarkThemeConfig
import com.maxim.testing.rules.MainDispatcherRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DarkThemeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: DarkThemeViewModel
    private lateinit var getDarkThemeConfigUseCase: GetDarkThemeConfigUseCase
    private lateinit var setDarkThemeConfigUseCase: SetDarkThemeConfigUseCase

    @Before
    fun setup() {
        getDarkThemeConfigUseCase = mockk()
        setDarkThemeConfigUseCase = mockk()
    }

    @Test
    fun `uiState emits Loading initially`() = runTest {
        every { getDarkThemeConfigUseCase() } returns emptyFlow()

        viewModel = DarkThemeViewModel(
            getDarkThemeConfigUseCase,
            setDarkThemeConfigUseCase
        )

        viewModel.uiState.test {
            val initial = awaitItem()

            assertEquals(
                DarkThemeLoadingStatus.Loading,
                initial.loadingStatus
            )

            expectNoEvents()
        }
    }

    @Test
    fun `uiState emits Loaded when config is emitted`() = runTest {
        val config = DarkThemeConfig.SYSTEM
        every { getDarkThemeConfigUseCase() } returns flowOf(config)

        viewModel = DarkThemeViewModel(
            getDarkThemeConfigUseCase,
            setDarkThemeConfigUseCase
        )

        viewModel.uiState.test {
            val initial = awaitItem()
            assertEquals(DarkThemeLoadingStatus.Loading, initial.loadingStatus)

            val loaded = awaitItem()
            assertEquals(DarkThemeLoadingStatus.Loaded, loaded.loadingStatus)
            assertEquals(config, loaded.currentConfig)

            expectNoEvents()
        }
    }

    @Test
    fun `uiState does not emit duplicate states for same config`() = runTest {
        val config = DarkThemeConfig.DARK

        every { getDarkThemeConfigUseCase() } returns flow {
            emit(config)
            emit(config)
        }

        viewModel = DarkThemeViewModel(
            getDarkThemeConfigUseCase,
            setDarkThemeConfigUseCase
        )

        viewModel.uiState.test {
            awaitItem()
            awaitItem()

            expectNoEvents()
        }
    }

    @Test
    fun `uiState stops collecting when unsubscribed`() = runTest {
        val flow = MutableSharedFlow<DarkThemeConfig>()
        every { getDarkThemeConfigUseCase() } returns flow

        viewModel = DarkThemeViewModel(
            getDarkThemeConfigUseCase,
            setDarkThemeConfigUseCase
        )

        viewModel.uiState.test {
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

        flow.emit(DarkThemeConfig.DARK)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onOptionClicked updates config`() = runTest {
        every { getDarkThemeConfigUseCase() } returns emptyFlow()
        coEvery { setDarkThemeConfigUseCase(any()) } just Runs

        viewModel = DarkThemeViewModel(
            getDarkThemeConfigUseCase,
            setDarkThemeConfigUseCase
        )

        viewModel.onOptionClicked(DarkThemeConfig.LIGHT)

        advanceUntilIdle()

        coVerify(exactly = 1) {
            setDarkThemeConfigUseCase(DarkThemeConfig.LIGHT)
        }
    }

}