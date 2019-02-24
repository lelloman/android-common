@file:Suppress("unused")

package com.lelloman.common.androidtestutils

import android.content.Context
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import com.lelloman.instrumentedtestutils.ViewActions
import com.lelloman.instrumentedtestutils.ViewAssertions

abstract class Screen {

    val context: Context = ApplicationProvider.getApplicationContext()

    fun viewVisible(@IdRes id: Int) = ViewAssertions.checkViewIsDisplayed(id)
    fun viewVisible(text: String) = ViewAssertions.checkViewWithTextIsDisplayed(text)

    fun string(@StringRes id: Int): String = context.getString(id)
    fun string(@StringRes id: Int, vararg arg: Any): String = context.getString(id, *arg)

    fun <T : Screen> showsScreen(screenCreator: () -> T): T = screenCreator()
}

fun <T : Screen> T.wait(seconds: Double) = apply { com.lelloman.instrumentedtestutils.wait(seconds) }

fun <T : Screen> T.clickOnViewWithText(@StringRes stringResId: Int) = apply {
    ViewActions.clickViewWithText(string(stringResId))
}

fun <T : Screen> T.rotateLeft() = apply { com.lelloman.instrumentedtestutils.rotateLeft() }
fun <T : Screen> T.rotateRight() = apply { com.lelloman.instrumentedtestutils.rotateRight() }
fun <T : Screen> T.rotateNatural() = apply { com.lelloman.instrumentedtestutils.rotateNatural() }

fun <T : Screen> T.closeKeyboard() = apply { Espresso.closeSoftKeyboard() }

fun <T : Screen> T.hasText(text: String) = apply { ViewAssertions.checkViewWithTextIsDisplayed(text) }