package com.maxim.ayon

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.maxim.ayon.root_navigation.RootNavigation
import com.maxim.ui.theme.AyonTheme

class MainActivity : AppCompatActivity() {

    private val appComponent by lazy { (application as AyonApplication).appComponent }
    private val mainViewModel: MainViewModel by viewModels { appComponent.viewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            !mainViewModel.isAppReady.value
        }

        mainViewModel.accept(InternalIntent.SetInitialAppState)

        enableEdgeToEdge()
        setContent {
            AyonTheme {
                RootNavigation(appComponent)
            }
        }
    }
}
