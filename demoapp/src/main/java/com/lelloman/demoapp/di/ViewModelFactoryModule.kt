package com.lelloman.demoapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lelloman.common.di.qualifiers.ViewModelKey
import com.lelloman.common.viewmodel.ViewModelFactory
import com.lelloman.demoapp.ui.main.MainViewModel
import com.lelloman.demoapp.ui.themeswitch.ThemeSwitchViewModel
import com.lelloman.demoapp.ui.webview.WebViewViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelFactoryModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ThemeSwitchViewModel::class)
    abstract fun bindThemeSwitchViewModel(themeSwitchViewModel: ThemeSwitchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WebViewViewModel::class)
    abstract fun bindWebViewViewModel(webViewViewModel: WebViewViewModel): ViewModel
}