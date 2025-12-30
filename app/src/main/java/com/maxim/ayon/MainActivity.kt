package com.maxim.ayon

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.maxim.ayon.root_navigation.RootNavigation
import com.maxim.ayon.util.isSystemInDarkTheme
import com.maxim.model.DarkThemeConfig
import com.maxim.ui.theme.AyonTheme
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val appComponent by lazy { (application as AyonApplication).appComponent }
    private val mainViewModel: MainViewModel by viewModels { appComponent.viewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            mainViewModel.isAppReady()
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(
                    isSystemInDarkTheme(),
                    mainViewModel.theme
                ) { systemDark, theme ->
                    when (theme) {
                        DarkThemeConfig.DARK -> true
                        DarkThemeConfig.LIGHT -> false
                        DarkThemeConfig.SYSTEM -> systemDark
                    }
                }
                    .distinctUntilChanged()
                    .collect { darkTheme ->
                        enableEdgeToEdge(
                            statusBarStyle = SystemBarStyle.auto(
                                lightScrim = Color.TRANSPARENT,
                                darkScrim = Color.TRANSPARENT,
                            ) { darkTheme },
                            navigationBarStyle = SystemBarStyle.auto(
                                lightScrim = lightScrim,
                                darkScrim = darkScrim,
                            ) { darkTheme },
                        )
                    }
            }
        }

        setContent {
            AyonTheme(
                themeConfig = mainViewModel.theme.collectAsStateWithLifecycle().value
            ) {
                RootNavigation(appComponent)
            }
        }
    }
}

private val lightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

private val darkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b)