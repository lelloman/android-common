package com.lelloman.demoapp.ui.main

import com.lelloman.common.viewmodel.BaseViewModel

abstract class MainViewModel(dependencies: BaseViewModel.Dependencies) : BaseViewModel(dependencies) {

    abstract fun onThemeSwitchClicked()

    abstract fun onWebViewClicked()
}