package com.lelloman.common.androidtestutils.matcher

import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description

class SwipeRefreshLayoutMatcher(private val isRefreshing: Boolean) :
    BoundedMatcher<View, SwipeRefreshLayout>(SwipeRefreshLayout::class.java) {

    override fun describeTo(description: Description) {
        description.appendText("check isRefreshing $isRefreshing")
    }

    override fun matchesSafely(item: SwipeRefreshLayout) = item.isRefreshing == isRefreshing
}