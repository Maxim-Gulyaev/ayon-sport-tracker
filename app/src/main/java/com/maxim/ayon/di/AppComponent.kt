package com.maxim.ayon.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModelProvider
import com.maxim.common.di.module.DispatcherModule
import com.maxim.common.di.module.LogModule
import com.maxim.common.util.AyonLogger
import com.maxim.data.di.DataModule
import com.maxim.database.di.DatabaseModule
import com.maxim.database.di.LocalDataSourceModule
import com.maxim.datastore.UserPreferencesDataSource
import com.maxim.datastore.data.UserPreferences
import com.maxim.datastore.di.DataStoreModule
import com.maxim.datastore.di.UserPreferencesDataSourceModule
import com.maxim.domain.di.UseCaseModule
import com.maxim.domain.repository.SettingsRepository
import com.maxim.domain.use_case.get_all_jogs.GetAllJogsUseCase
import com.maxim.domain.use_case.get_app_language.GetAppLanguageUseCase
import com.maxim.domain.use_case.save_jog.SaveJogUseCase
import com.maxim.domain.use_case.set_app_language.SetAppLanguageUseCase
import com.maxim.home.di.utils.HomeDependencies
import com.maxim.run.di.utils.RunDependencies
import com.maxim.settings.di.utils.SettingsDependencies
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        DataStoreModule::class,
        DatabaseModule::class,
        LocalDataSourceModule::class,
        UserPreferencesDataSourceModule::class,
        DataModule::class,
        UseCaseModule::class,
        MainViewModelModule::class,
        DispatcherModule::class,
        LogModule::class,
    ]
)
interface AppComponent :
    SettingsDependencies,
    RunDependencies,
    HomeDependencies {

    fun viewModelFactory(): ViewModelProvider.Factory

    override fun userPreferencesDataStore(): DataStore<UserPreferences>

    override fun userPreferencesDataSource(): UserPreferencesDataSource

    override fun settingsRepository(): SettingsRepository

    override fun getAppLanguageUseCase(): GetAppLanguageUseCase

    override fun setAppLanguageUseCase(): SetAppLanguageUseCase

    override fun saveJogUseCase(): SaveJogUseCase

    override fun getAllJogsUseCase(): GetAllJogsUseCase

    override fun logger(): AyonLogger

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent
    }
}