package com.maxim.ayon.bottom_bar_navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.maxim.ayon.R
import com.maxim.ayon.di.AppComponent
import com.maxim.home.di.utils.HomeComponentHolder
import com.maxim.home.screen.home_screen.HomeScreen
import com.maxim.home.screen.home_screen.HomeScreenViewModel
import com.maxim.navigation.BottomBarNavigationRoute
import com.maxim.navigation.bottomBarItems
import com.maxim.profile.profile_screen.ProfileScreen

@Composable
fun BottomBarNavigation(
    appComponent: AppComponent,
    navigateRunScreen: () -> Unit,
    navigateSettingsScreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination.routeOrNull

    Scaffold(
        topBar = {
            AyonTopAppBar(
                titleRes = topAppBarTitleRes(currentRoute),
                currentRoute = currentRoute,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                ),
                onSettingsIconClick = { navigateSettingsScreen() }
            )
        },
        bottomBar = {
            NavigationBar() {
                bottomBarItems.forEachIndexed { index, destination ->
                    AyonNavigationBarItem(
                        currentDestination = currentDestination,
                        destination = destination,
                        onClick = {
                            when (destination) {
                                BottomBarNavigationRoute.Run -> navigateRunScreen()
                                else -> {
                                    navController.navigate(destination) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NavHost(
                navController = navController,
                startDestination = BottomBarNavigationRoute.Home
            ) {
                composable<BottomBarNavigationRoute.Home> { entry ->
                    val homeComponentHolder: HomeComponentHolder = viewModel(
                        viewModelStoreOwner = entry,
                        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                                @Suppress("UNCHECKED_CAST")
                                return HomeComponentHolder(appComponent) as T
                            }
                        }
                    )

                    val homeComponent = homeComponentHolder.homeComponent

                    val viewModelFactory = homeComponent.viewModelFactory()

                    val homeScreenViewModel: HomeScreenViewModel = viewModel(
                        viewModelStoreOwner = entry,
                        factory = viewModelFactory
                    )
                    HomeScreen(homeScreenViewModel)
                }

                composable<BottomBarNavigationRoute.Profile> {
                    ProfileScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AyonTopAppBar(
    currentRoute: String?,
    @StringRes titleRes: Int,
    onSettingsIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(titleRes)) },
        actions = {
            if (currentRoute == BottomBarNavigationRoute.Profile::class.qualifiedName) {
                IconButton(onClick = onSettingsIconClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_settings),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
        colors = colors,
    )
}

@Composable
private fun RowScope.AyonNavigationBarItem(
    currentDestination: NavDestination?,
    destination: BottomBarNavigationRoute,
    onClick: () -> Unit,
) {
    NavigationBarItem(
        selected = currentDestination?.hierarchy?.any { it.route == destination::class.qualifiedName } == true,
        icon = {
            Icon(
                painter = painterResource(destination.icon),
                contentDescription = "$destination icon"
            )
        },
        onClick = onClick,
        label = {
            Text(text = stringResource(destination.title))
        }
    )
}

private val NavDestination?.routeOrNull: String?
    get() = this?.hierarchy?.firstOrNull()?.route

@Composable
private fun topAppBarTitleRes(currentRoute: String?) =
    remember(currentRoute) {
        when (currentRoute) {
            BottomBarNavigationRoute.Profile::class.qualifiedName -> R.string.top_bar_profile
            BottomBarNavigationRoute.Home::class.qualifiedName -> R.string.top_bar_run_tracker
            else -> R.string.top_bar_run_tracker
        }
    }