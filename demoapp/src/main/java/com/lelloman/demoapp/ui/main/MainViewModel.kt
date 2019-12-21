package com.lelloman.demoapp.ui.main

import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.demoapp.commands.OpenThemesSwitchScreenCommand
import com.lelloman.demoapp.commands.OpenWebViewScreenCommand

class MainViewModel(dependencies: Dependencies) : BaseViewModel(dependencies) {

    fun onThemeSwitchClicked() = emitCommand(OpenThemesSwitchScreenCommand)

    fun onWebViewClicked() = emitCommand(OpenWebViewScreenCommand)
}