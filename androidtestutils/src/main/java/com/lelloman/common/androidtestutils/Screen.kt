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

inline fun <reified T : Screen> T.wait(seconds: Double) = apply { com.lelloman.common.androidtestutils.wait(seconds) }

inline fun <reified T : Screen> T.clickOnViewWithText(@StringRes stringResId: Int) = apply {
    ViewActions.clickViewWithText(string(stringResId))
}

inline fun <reified T : Screen> T.rotateLeft() = apply { com.lelloman.instrumentedtestutils.rotateLeft() }
inline fun <reified T : Screen> T.rotateRight() = apply { com.lelloman.instrumentedtestutils.rotateRight() }
inline fun <reified T : Screen> T.rotateNatural() = apply { com.lelloman.instrumentedtestutils.rotateNatural() }

inline fun <reified T : Screen> T.closeKeyboard() = apply { Espresso.closeSoftKeyboard() }

inline fun <reified T : Screen> T.hasText(text: String) = apply { ViewAssertions.checkViewWithTextIsDisplayed(text) }
