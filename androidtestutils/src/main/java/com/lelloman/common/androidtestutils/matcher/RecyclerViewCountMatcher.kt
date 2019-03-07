package com.lelloman.common.androidtestutils.matcher

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description

class RecyclerViewCountMatcher(private val expectedCount: Int) :
    BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {

    override fun describeTo(description: Description) {
        description.appendText("check RecyclerView count $expectedCount")
    }

    override fun matchesSafely(item: RecyclerView) = item.adapter!!.itemCount == expectedCount
}