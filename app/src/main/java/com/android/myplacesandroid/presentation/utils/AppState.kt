package com.android.myplacesandroid.presentation.utils

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.android.myplacesandroid.OpenForTesting
import com.android.myplacesandroid.presentation.screen.destinations.HomeScreenDestination
import com.android.myplacesandroid.presentation.screen.destinations.SettingsScreenDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OpenForTesting
class AppState(
    val scaffoldState: ScaffoldState,
    val navController: NavHostController,
) {

    val bottomBarRoutes = listOf(
        HomeScreenDestination,
        SettingsScreenDestination
    )

    // Logic to decide when to show the bottom bar
    val isRootRoute: Boolean
        @Composable get() {
            for (d in bottomBarRoutes) {
                if (d.route == navController
                        .currentBackStackEntryAsState().value?.destination?.route
                ) {
                    return true
                }
            }
            return false
        }

    // Navigation logic, which is a type of UI logic
    fun navigateToBottomBarRoute(route: String) {
        navController.navigate(route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }

    // Show snackbar using Resources
    fun showSnackbar(message: String) {
    }
}

@Composable
fun rememberAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    //resources: Resources = LocalContext.current.resources,
) = remember(navController, scaffoldState) {
    AppState(scaffoldState, navController)
}

