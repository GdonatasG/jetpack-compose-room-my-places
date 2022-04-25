package com.android.myplacesandroid.presentation.screen.root.home

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.filters.MediumTest
import com.android.myplacesandroid.HiltTestActivity
import com.android.myplacesandroid.MainActivity
import com.android.myplacesandroid.presentation.theme.MyPlacesAndroidTheme
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@HiltAndroidTest
@MediumTest
class HomeScreenTest {
    lateinit var navigator: DestinationsNavigator
    lateinit var viewModel: HomeViewModel

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        navigator = Mockito.mock(DestinationsNavigator::class.java)
        viewModel = Mockito.mock(HomeViewModel::class.java)
    }

    private fun initScreen() {
        composeTestRule.setContent {
            MyPlacesAndroidTheme {
                HomeScreen(navigator = navigator, viewModel = viewModel)
            }
        }
    }

    @Test
    fun viewModelIsLoading_displaysCircularProgressIndicator() {
        Mockito.`when`(viewModel.state).thenReturn(HomeState(isLoading = true))
        initScreen()

        composeTestRule.onNodeWithTag(HomeScreenTestTags.CIRCULAR_PROGRESS_INDICATOR)
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag(HomeScreenTestTags.LOADED_VIEW).assertDoesNotExist()

    }

    @Test
    fun viewModelIsNotLoading_displaysLoadedView() {
        Mockito.`when`(viewModel.state).thenReturn(HomeState(isLoading = false))
        initScreen()

        composeTestRule.onNodeWithTag(HomeScreenTestTags.CIRCULAR_PROGRESS_INDICATOR)
            .assertDoesNotExist()
        composeTestRule.onNodeWithTag(HomeScreenTestTags.LOADED_VIEW).assertIsDisplayed()

    }

    @Test
    fun sizeControls_onClickUp_onlySingleClickable() {
        Mockito.`when`(viewModel.state).thenReturn(HomeState(isLoading = false))
        initScreen()

        composeTestRule.onNodeWithTag(HomeScreenTestTags.SIZE_CONTROL_UP).performClick()
        composeTestRule.onNodeWithTag(HomeScreenTestTags.SIZE_CONTROL_UP).assertIsNotEnabled()


    }

    @Test
    fun sizeControls_onClickDown_onlySingleClickable() {
        Mockito.`when`(viewModel.state).thenReturn(HomeState(isLoading = false))
        initScreen()

        composeTestRule.onNodeWithTag(HomeScreenTestTags.SIZE_CONTROL_DOWN).performClick()
        composeTestRule.onNodeWithTag(HomeScreenTestTags.SIZE_CONTROL_DOWN).assertIsNotEnabled()


    }

    @Test
    fun sizeControls_onClickUpAndDown_bothControlsAreEnabled() {
        Mockito.`when`(viewModel.state).thenReturn(HomeState(isLoading = false))
        initScreen()

        composeTestRule.onNodeWithTag(HomeScreenTestTags.SIZE_CONTROL_UP).performClick()
        composeTestRule.onNodeWithTag(HomeScreenTestTags.SIZE_CONTROL_DOWN).performClick()

        composeTestRule.onNodeWithTag(HomeScreenTestTags.SIZE_CONTROL_UP).assertIsEnabled()
        composeTestRule.onNodeWithTag(HomeScreenTestTags.SIZE_CONTROL_DOWN).assertIsEnabled()
    }

    @Test
    fun sizeControls_onClickDownAndUp_bothControlsAreEnabled() {
        Mockito.`when`(viewModel.state).thenReturn(HomeState(isLoading = false))
        initScreen()

        composeTestRule.onNodeWithTag(HomeScreenTestTags.SIZE_CONTROL_DOWN).performClick()
        composeTestRule.onNodeWithTag(HomeScreenTestTags.SIZE_CONTROL_UP).performClick()

        composeTestRule.onNodeWithTag(HomeScreenTestTags.SIZE_CONTROL_DOWN).assertIsEnabled()
        composeTestRule.onNodeWithTag(HomeScreenTestTags.SIZE_CONTROL_UP).assertIsEnabled()
    }


}