@file:Suppress("unused")

package com.lelloman.instrumentedtestutils

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matcher
import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing


fun <T> whenever(methodCall: T): OngoingStubbing<T> = Mockito.`when`(methodCall)

@Suppress("UNCHECKED_CAST")
fun <T> nullAsT(): T = null as T

inline fun <reified T : Any> nonNullAny(): T {
    return Mockito.any(T::class.java) ?: nullAsT()
}

fun ViewInteraction.checkMatches(matcher: Matcher<View>): ViewInteraction = check(matches(matcher))

fun onUiThread(action: () -> Unit) = InstrumentationRegistry
    .getInstrumentation()
    .runOnMainSync(action)

fun wait(seconds: Double) = Thread.sleep((seconds * 1000).toLong())

fun viewWithId(id: Int): ViewInteraction = Espresso.onView(ViewMatchers.withId(id))

fun viewWithText(text: String): ViewInteraction = Espresso.onView(ViewMatchers.withText(text))