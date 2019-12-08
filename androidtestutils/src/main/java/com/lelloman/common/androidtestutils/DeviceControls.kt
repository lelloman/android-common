@file:Suppress("unused")

package com.lelloman.common.androidtestutils

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice

fun rotateNatural() =
    UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).setOrientationNatural()

fun rotateLeft() =
    UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).setOrientationLeft()

fun rotateRight() =
    UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).setOrientationRight()
