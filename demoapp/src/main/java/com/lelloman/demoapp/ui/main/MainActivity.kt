package com.lelloman.demoapp.ui.main

import com.lelloman.common.view.BaseActivity
import com.lelloman.common.viewmodel.command.Command
import com.lelloman.demoapp.R
import com.lelloman.demoapp.commands.OpenThemesSwitchScreenCommand
import com.lelloman.demoapp.commands.OpenWebViewScreenCommand
import com.lelloman.demoapp.databinding.ActivityMainBinding
import com.lelloman.demoapp.ui.themeswitch.ThemeSwitchActivity
import com.lelloman.demoapp.ui.webview.WebViewActivity
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override val viewModel by viewModel<MainViewModel>()

    override val layoutResId = R.layout.activity_main

    override fun setViewModel(binding: ActivityMainBinding, viewModel: MainViewModel) {
        binding.viewModel = viewModel
    }

    override fun onUnhandledCommand(command: Command) = when (command) {
        is OpenThemesSwitchScreenCommand -> ThemeSwitchActivity.start(this)
        is OpenWebViewScreenCommand -> WebViewActivity.start(this)
        else -> Unit
    }
}
