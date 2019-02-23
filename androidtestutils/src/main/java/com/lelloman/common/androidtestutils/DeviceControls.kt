@file:Suppress("unused")

package com.lelloman.instrumentedtestutils

import android.support.test.uiautomator.UiDevice
import androidx.test.platform.app.InstrumentationRegistry

fun rotateNatural() = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).setOrientationNatural()

fun rotateLeft() = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).setOrientationLeft()

fun rotateRight() = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).setOrientationRight()
