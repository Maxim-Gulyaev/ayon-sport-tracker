package com.maxim.datastore

import androidx.datastore.core.DataStore
import com.maxim.common.util.AyonLogger
import com.maxim.datastore.data.UserPreferences
import com.maxim.datastore.model.AppLanguageEntity
import com.maxim.datastore.model.DarkThemeConfigEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val DEFAULT_USER_PREFERENCES = UserPreferences.getDefaultInstance()

class UserPreferencesDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<UserPreferences>,
    private val logger: AyonLogger,
): UserPreferencesDataSource {

    override val appLanguage: Flow<AppLanguageEntity> =
        dataStore.data
            .catch { throwable ->
                emit(DEFAULT_USER_PREFERENCES)
                logger.e("UserPreferencesDataSourceImpl appLanguage failed", throwable)
            }
            .map { it.appLanguage.toDomain() }

    override suspend fun updateAppLanguage(language: AppLanguageEntity) {
        dataStore.updateData { prefs ->
            prefs.toBuilder()
                .setAppLanguage(language.toProto())
                .build()
        }
    }


    override val darkThemeConfig: Flow<DarkThemeConfigEntity> =
        dataStore.data
            .catch { throwable ->
                emit(DEFAULT_USER_PREFERENCES)
                logger.e("UserPreferencesDataSourceImpl darkThemeConfig failed", throwable)
            }
            .map { it.darkThemeConfig.toDomain() }

    override suspend fun updateDarkThemeConfig(config: DarkThemeConfigEntity) {
        dataStore.updateData { prefs ->
            prefs.toBuilder()
                .setDarkThemeConfig(config.toProto())
                .build()
        }
    }
}
