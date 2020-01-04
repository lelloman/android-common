package com.lelloman.demoapp.ui.main

import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.common.viewmodel.command.ShowSnackCommand
import com.lelloman.demoapp.commands.OpenScrollBehaviorScreenCommand
import com.lelloman.demoapp.commands.OpenThemesSwitchScreenCommand
import com.lelloman.demoapp.commands.OpenWebViewScreenCommand

class MainViewModel(dependencies: Dependencies) : BaseViewModel(dependencies) {

    fun onThemeSwitchClicked() = emitCommand(OpenThemesSwitchScreenCommand)

    fun onWebViewClicked() = emitCommand(OpenWebViewScreenCommand)

    fun onScrollBehaviorClicked() = emitCommand(OpenScrollBehaviorScreenCommand)

    fun onShowSnackClicked() = emitCommand(ShowSnackCommand("Hello Snackbar"))

    fun onChangeScreenTitleClicked() = throttledAction {
        val title = if (screenTitle.value == "New title!") {
            "Yet another title!"
        } else {
            "New title!"
        }
        setScreenTitle(title)
    }
}
