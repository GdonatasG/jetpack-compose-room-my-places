package com.android.myplacesandroid.presentation.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.filters.MediumTest
import com.android.myplacesandroid.HiltTestActivity
import com.android.myplacesandroid.MainActivity
import com.android.myplacesandroid.di.AppModule
import com.android.myplacesandroid.di.DependencyContainer
import com.android.myplacesandroid.presentation.screen.destinations.AddNewPlaceScreenDestination
import com.android.myplacesandroid.presentation.screen.destinations.HomeScreenDestination
import com.android.myplacesandroid.presentation.screen.destinations.SettingsScreenDestination
import com.android.myplacesandroid.presentation.screen.root.MainScreenTestTags
import com.android.myplacesandroid.presentation.theme.MyPlacesAndroidTheme
import com.android.myplacesandroid.presentation.utils.AppState
import com.android.myplacesandroid.presentation.utils.rememberAppState
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@HiltAndroidTest
@MediumTest
@OptIn(ExperimentalAnimationApi::class)
class MainScreenTest {
    private lateinit var appState: AppState
    private lateinit var navController: NavHostController

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()


    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.setContent {
            navController = rememberNavController()
            appState = rememberAppState(navController = navController)
            MyPlacesAndroidTheme {
                MainScreen(appState = appState)
            }
        }
    }

    @Test
    fun clickFloatingActionButton_navigatesToAddNewPlaceScreen_hidesBottomBar_hidesFloatingButton() {
        composeTestRule.onNodeWithTag(MainScreenTestTags.FLOATING_ACTION_BUTTON).performClick()

        assertThat(navController.currentDestination!!.route).isEqualTo(AddNewPlaceScreenDestination.route)
        composeTestRule.onNodeWithTag(MainScreenTestTags.BOTTOM_BAR).assertDoesNotExist()
        composeTestRule.onNodeWithTag(MainScreenTestTags.FLOATING_ACTION_BUTTON).assertDoesNotExist()

    }
}