package com.maxim.ayon.root_navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maxim.ayon.bottom_bar_navigation.BottomBarNavigation
import com.maxim.ayon.di.AppComponent
import com.maxim.navigation.RootNavigationRoute
import com.maxim.run.navigation.runGraph
import com.maxim.settings.navigation.settingsGraph

@Composable
fun RootNavigation(
    appComponent: AppComponent,
) {
    val navController = rememberNavController()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = RootNavigationRoute.BottomBarScreen
        ) {
            composable<RootNavigationRoute.BottomBarScreen> {
                BottomBarNavigation(
                    appComponent = appComponent,
                    navigateRunScreen = { navController.navigate(RootNavigationRoute.RunScreen) },
                    navigateSettingsScreen = { navController.navigate(RootNavigationRoute.SettingsScreen) }
                )
            }

            runGraph(
                appComponent = appComponent,
                quitRunScreen = { navController.popBackStack() }
            )

            settingsGraph(navController, appComponent)
        }
    }
}