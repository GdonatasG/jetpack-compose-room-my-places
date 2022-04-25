package com.android.myplacesandroid.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.android.myplacesandroid.LocalDependencyContainer
import com.android.myplacesandroid.presentation.screen.destinations.AddNewPlaceScreenDestination
import com.android.myplacesandroid.presentation.screen.destinations.HomeScreenDestination
import com.android.myplacesandroid.presentation.screen.destinations.SettingsScreenDestination
import com.android.myplacesandroid.presentation.screen.root.MainScreenTestTags
import com.android.myplacesandroid.presentation.utils.AppState
import com.ramcosta.composedestinations.DestinationsNavHost

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@ExperimentalAnimationApi
fun MainScreen(appState: AppState = LocalDependencyContainer.current.getAppState) {
    Scaffold(
        scaffoldState = appState.scaffoldState,
        bottomBar = {
            if (appState.isRootRoute) {
                BottomBar(appState = appState)
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            if (appState.isRootRoute) {
                FloatingActionButton(
                    modifier = Modifier.testTag(MainScreenTestTags.FLOATING_ACTION_BUTTON),
                    onClick = {
                        appState.navController.navigate(AddNewPlaceScreenDestination.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                    )
                }
            }
        },
        isFloatingActionButtonDocked = true
    ) {
        DestinationsNavHost(
            modifier = Modifier.padding(
                top = it.calculateTopPadding(),
                bottom = it.calculateBottomPadding()
            ),
            navGraph = NavGraphs.root,
            navController = appState.navController,
            dependenciesContainerBuilder = {
                /*if (HomeScreenDestination.route == destination.route) {
                    val parentEntry =
                        remember { navController.getBackStackEntry(HomeScreenDestination.route) }
                    dependency(
                        androidx.lifecycle.viewmodel.compose.viewModel<HomeViewModel>(
                            parentEntry
                        )
                    )
                }*/
            }
        )
    }
}

@Composable
fun BottomBar(appState: AppState) {
    BottomNavigation(
        modifier = Modifier.testTag(MainScreenTestTags.BOTTOM_BAR)
    ) {
        val navBackStackEntry by appState.navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        appState.bottomBarRoutes.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    when (screen.route) {
                        HomeScreenDestination.route -> {
                            Icon(
                                Icons.Filled.Home,
                                contentDescription = null
                            )
                        }
                        SettingsScreenDestination.route -> {
                            Icon(
                                Icons.Filled.Settings,
                                contentDescription = null
                            )
                        }
                    }
                },
                label = {
                    Text(
                        when (screen.route) {
                            HomeScreenDestination.route -> {
                                "My Places"
                            }
                            SettingsScreenDestination.route -> {
                                "Settings"
                            }
                            else -> {
                                ""
                            }
                        }
                    )
                },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    appState.navigateToBottomBarRoute(screen.route)
                }
            )
        }
    }
}