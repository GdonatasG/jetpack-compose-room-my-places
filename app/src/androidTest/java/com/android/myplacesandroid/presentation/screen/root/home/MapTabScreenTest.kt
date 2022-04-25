package com.android.myplacesandroid.presentation.screen.root.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.filters.MediumTest
import com.android.myplacesandroid.HiltTestActivity
import com.android.myplacesandroid.MainActivity
import com.android.myplacesandroid.presentation.theme.MyPlacesAndroidTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito


@HiltAndroidTest
@MediumTest
@OptIn(ExperimentalPermissionsApi::class)
class MapTabScreenTest {
    private lateinit var viewModel: HomeViewModel
    lateinit var permissionsState: MultiplePermissionsState

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        viewModel = Mockito.mock(HomeViewModel::class.java)
        permissionsState = Mockito.mock(MultiplePermissionsState::class.java)
    }

    private fun initScreen() {
        composeTestRule.setContent {
            MyPlacesAndroidTheme {
                MapTabScreen(viewModel, permissionsState)
            }
        }
    }

    @Test
    fun allPermissionsGranted_displaysGoogleMap() {
        Mockito.`when`(permissionsState.allPermissionsGranted).thenReturn(true)
        Mockito.`when`(viewModel.state).thenReturn(HomeState())
        initScreen()

        composeTestRule.onNodeWithTag(MapTabScreenTestTags.GOOGLE_MAP).assertIsDisplayed()
        composeTestRule.onNodeWithTag(MapTabScreenTestTags.PERMISSIONS_ERROR_VIEW)
            .assertDoesNotExist()
    }

    @Test
    fun notGrantedPermissions_displaysPermissionsErrorView() {
        Mockito.`when`(permissionsState.allPermissionsGranted).thenReturn(false)
        Mockito.`when`(viewModel.state).thenReturn(HomeState())
        initScreen()

        composeTestRule.onNodeWithTag(MapTabScreenTestTags.GOOGLE_MAP).assertDoesNotExist()
        composeTestRule.onNodeWithTag(MapTabScreenTestTags.PERMISSIONS_ERROR_VIEW)
            .assertIsDisplayed()
    }
}