package com.lelloman.common

import android.app.Application
import com.lelloman.common.androidtestutils.TestApplicationRunner

class TestApplicationRunner : TestApplicationRunner() {

    override val testAppClassName: String = Application::class.java.name

}