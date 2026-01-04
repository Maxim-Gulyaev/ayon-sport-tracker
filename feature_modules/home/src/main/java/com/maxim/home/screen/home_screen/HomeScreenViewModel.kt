package com.maxim.home.screen.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxim.common.result.Result
import com.maxim.common.result.asResult
import com.maxim.common.util.AyonLogger
import com.maxim.domain.use_case.get_all_jogs.GetAllJogsUseCase
import com.maxim.model.Jog
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class HomeScreenViewModel @Inject constructor(
    getAllJogsUseCase: GetAllJogsUseCase,
    log: AyonLogger,
): ViewModel() {

    val uiState = uiState(getAllJogsUseCase, log)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeScreenUiState.Loading
        )
}


private fun uiState(
    getAllJogsUseCase: GetAllJogsUseCase,
    log: AyonLogger,
) : Flow<HomeScreenUiState> {

    val jogs: Flow<List<Jog>> = getAllJogsUseCase()

    return jogs
        .asResult()
        .map { result ->
            when (result) {
                is Result.Success -> {
                    HomeScreenUiState.Success(
                        jogList = result.data.toImmutableList()
                    )
                }

                is Result.Loading -> HomeScreenUiState.Loading

                is Result.Error -> {
                    log.e("getAllJogsUseCase failed", result.exception)
                    HomeScreenUiState.Error(throwable = result.exception)
                }
            }
        }
}