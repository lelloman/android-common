@file:Suppress("unused")

package com.lelloman.instrumentedtestutils

import android.content.Context
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.core.app.ApplicationProvider

abstract class Screen {

    private val context: Context = ApplicationProvider.getApplicationContext()

    protected fun viewVisible(@IdRes id: Int) = ViewAssertions.checkViewIsDisplayed(id)
    protected fun viewVisible(text: String) = ViewAssertions.checkViewWithTextIsDisplayed(text)

    protected fun string(@StringRes id: Int): String = context.getString(id)
    protected fun string(@StringRes id: Int, vararg arg: Any): String = context.getString(id, *arg)
}