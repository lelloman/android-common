@file:Suppress("DeprecatedCallableAddReplaceWith", "unused")

package com.lelloman.instrumentedtestutils

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.platform.app.InstrumentationRegistry
import com.lelloman.common.androidtestutils.viewWithId
import com.lelloman.common.androidtestutils.viewWithText


@Deprecated(message = "Use ViewActions instead")
fun swipeBottomSheetUp(): ViewAction = GeneralSwipeAction(
    Swipe.FAST,
    CoordinatesProvider {
        floatArrayOf(200f, 1750f)
    },
    CoordinatesProvider {
        floatArrayOf(400f, 700f)
    },
    Press.FINGER
)


object ViewActions {
    fun swipeBottomSheetUp(): ViewAction = GeneralSwipeAction(
        Swipe.FAST,
        CoordinatesProvider {
            floatArrayOf(200f, 1750f)
        },
        CoordinatesProvider {
            floatArrayOf(400f, 700f)
        },
        Press.FINGER
    )

    fun clickOnRecyclerViewItem(position: Int, recyclerViewId: Int): ViewInteraction =
        viewWithId(recyclerViewId)
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click()))

    fun swipeLeft(id: Int) {
        viewWithId(id).perform(
            GeneralSwipeAction(
                Swipe.FAST,
                GeneralLocation.CENTER_LEFT,
                GeneralLocation.CENTER_RIGHT,
                Press.FINGER
            )
        )
    }

    fun swipeRight(id: Int) {
        viewWithId(id).perform(
            GeneralSwipeAction(
                Swipe.FAST,
                GeneralLocation.CENTER_RIGHT,
                GeneralLocation.CENTER_LEFT,
                Press.FINGER
            )
        )
    }

    fun typeInEditText(id: Int, text: String) {
        viewWithId(id).perform(typeText(text))
    }

    fun openOverflowMenu() {
        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    fun clickView(id: Int) {
        viewWithId(id).perform(click())
    }

    fun clickViewWithText(text: String) {
        viewWithText(text).perform(click())
    }
}