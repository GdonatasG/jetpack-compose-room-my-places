package com.android.myplacesandroid.presentation.screen.root.home

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.filters.MediumTest
import com.android.myplacesandroid.HiltTestActivity
import com.android.myplacesandroid.MainActivity
import com.android.myplacesandroid.domain.model.Description
import com.android.myplacesandroid.domain.model.MyPlace
import com.android.myplacesandroid.domain.model.Title
import com.android.myplacesandroid.presentation.theme.MyPlacesAndroidTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@MediumTest
class ListTabScreenTest {
    private val places = listOf(
        MyPlace(5.5, 8.5, Title("Place1"), Description("Description1"), ByteArray(0)),
        MyPlace(10.5, 11.5, Title("Place2"), Description("Description2"), ByteArray(0)),
        MyPlace(-20.5, -10.1, Title("Place3"), Description("Description3"), ByteArray(0))
    )

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    private fun initScreen(places: List<MyPlace> = emptyList()) {
        composeTestRule.setContent {
            MyPlacesAndroidTheme {
                ListTabScreen(places)
            }
        }
    }

    @Test
    fun emptyPlacesList_displaysNoPlacesView() {
        initScreen()

        composeTestRule.onNodeWithTag(ListTabScreenTestTags.NO_PLACES_VIEW).assertIsDisplayed()
        composeTestRule.onNodeWithTag(ListTabScreenTestTags.PLACES_LIST_VIEW).assertDoesNotExist()
    }

    @Test
    fun notEmptyPlacesList_displaysPlacesListView() {
        initScreen(places)

        composeTestRule.onNodeWithTag(ListTabScreenTestTags.NO_PLACES_VIEW).assertDoesNotExist()

        composeTestRule.onNodeWithTag(ListTabScreenTestTags.PLACES_LIST_VIEW).assertIsDisplayed()
        composeTestRule.onNodeWithTag(ListTabScreenTestTags.PLACES_LIST_VIEW).onChildren()
            .assertCountEquals(places.size)
        places.forEachIndexed { index, place ->
            composeTestRule.onNodeWithTag(ListTabScreenTestTags.PLACES_LIST_VIEW).onChildAt(index)
                .assertTextContains(place.title.getDataOrFailedValue()!!)
            composeTestRule.onNodeWithTag(ListTabScreenTestTags.PLACES_LIST_VIEW).onChildAt(index)
                .assertTextContains(place.description.getDataOrFailedValue()!!)
        }
    }


}