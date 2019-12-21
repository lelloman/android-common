package com.lelloman.demoapp.di

import com.lelloman.common.di.KoinModuleFactory
import com.lelloman.demoapp.ui.main.MainViewModel
import com.lelloman.demoapp.ui.themeswitch.ThemeSwitchViewModel
import com.lelloman.demoapp.ui.webview.WebViewViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

class ViewModelModuleFactory : KoinModuleFactory {

    override fun makeKoinModule(override: Boolean) = module(override = override) {
        viewModel {
            MainViewModel(
                dependencies = get()
            )
        }
        viewModel {
            ThemeSwitchViewModel(
                dependencies = get()
            )
        }
        viewModel {
            WebViewViewModel(
                dependencies = get(),
                pdfInterceptor = get(),
                adBlockInterceptor = get()
            )
        }
    }
}