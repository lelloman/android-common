package com.lelloman.demoapp.di

import androidx.lifecycle.ViewModel
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.common.webview.interceptor.AdBlockInterceptor
import com.lelloman.common.webview.interceptor.pdf.PdfInterceptor
import com.lelloman.demoapp.ui.main.MainViewModel
import com.lelloman.demoapp.ui.main.MainViewModelImpl
import com.lelloman.demoapp.ui.themeswitch.ThemeSwitchViewModel
import com.lelloman.demoapp.ui.themeswitch.ThemeSwitchViewModelImpl
import com.lelloman.demoapp.ui.webview.WebViewViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import javax.inject.Singleton

@Module
class ViewModelModule {

    @Singleton
    @Provides
    fun provideMap(): Map<Class<out ViewModel>, Provider<out ViewModel>> = mutableMapOf()

    @Provides
    fun provideMainViewModel(
        dependencies: BaseViewModel.Dependencies
    ): MainViewModel = MainViewModelImpl(
        dependencies = dependencies
    )

    @Provides
    fun provideThemeSwitchViewModel(
        dependencies: BaseViewModel.Dependencies
    ): ThemeSwitchViewModel = ThemeSwitchViewModelImpl(
        dependencies = dependencies
    )

    @Provides
    fun provideWebViewViewModel(
        dependencies: BaseViewModel.Dependencies,
        pdfInterceptor: PdfInterceptor,
        adBlockInterceptor: AdBlockInterceptor
    ) = WebViewViewModel(
        dependencies = dependencies,
        pdfInterceptor = pdfInterceptor,
        adBlockInterceptor = adBlockInterceptor
    )
}