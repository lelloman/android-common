package com.lelloman.demoapp.di

import com.lelloman.common.di.KoinModuleFactory
import com.lelloman.demoapp.ui.main.MainViewModel
import com.lelloman.demoapp.ui.themeswitch.ThemeSwitchViewModel
import com.lelloman.demoapp.ui.webview.WebViewViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

class ViewModelModule : KoinModuleFactory {

    override fun makeKoinModule() = module {
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