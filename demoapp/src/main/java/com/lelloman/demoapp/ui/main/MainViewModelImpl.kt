package com.lelloman.demoapp.ui.main

import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.demoapp.navigation.DemoAppScreens

class MainViewModelImpl(dependencies: BaseViewModel.Dependencies) : MainViewModel(dependencies) {

    override fun onThemeSwitchClicked() = navigate(DemoAppScreens.THEMES_SWITCH)
}