package com.lelloman.demoapp.di

import com.lelloman.common.view.InjectableActivity
import com.lelloman.demoapp.ui.main.MainActivity
import com.lelloman.demoapp.ui.themeswitch.ThemeSwitchActivity
import com.lelloman.demoapp.ui.webview.WebViewActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
interface AndroidContributes {

    @ContributesAndroidInjector
    fun contributeBaseActivity(): InjectableActivity

    @ContributesAndroidInjector
    fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    fun contributeThemeSwitchActivity(): ThemeSwitchActivity

    @ContributesAndroidInjector
    fun contributeWebViewActivity(): WebViewActivity
}