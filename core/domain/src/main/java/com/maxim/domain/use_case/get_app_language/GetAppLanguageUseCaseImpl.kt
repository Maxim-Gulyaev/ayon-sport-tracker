package com.maxim.domain.use_case.get_app_language

import com.maxim.domain.repository.SettingsRepository
import com.maxim.model.AppLanguage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAppLanguageUseCaseImpl @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : GetAppLanguageUseCase {

    override operator fun invoke(): Flow<AppLanguage> = settingsRepository.getAppLanguage()
}