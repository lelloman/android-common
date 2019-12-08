package com.lelloman.demoapp.ui.main

import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.demoapp.navigation.DemoAppScreens

class MainViewModel(dependencies: Dependencies) : BaseViewModel(dependencies) {

    fun onThemeSwitchClicked() = navigate(DemoAppScreens.THEMES_SWITCH)

    fun onWebViewClicked() = navigate(DemoAppScreens.WEB_VIEW)
}