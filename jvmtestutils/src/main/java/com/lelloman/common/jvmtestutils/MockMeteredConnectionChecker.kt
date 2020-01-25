package com.lelloman.common.jvmtestutils

import com.lelloman.common.data.MeteredConnectionChecker

class MockMeteredConnectionChecker(var isNetworkMeteredValue: Boolean = false) :
    MeteredConnectionChecker {

    override fun isNetworkMetered() = isNetworkMeteredValue
}