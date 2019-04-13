package com.lelloman.demoapp.di

import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.common.http.HttpModule
import com.lelloman.common.settings.BaseSettingsModule
import com.lelloman.common.webview.WebViewModule
import com.lelloman.demoapp.DemoApplication
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidContributes::class,
        AndroidInjectionModule::class,
        BaseApplicationModule::class,
        BaseSettingsModule::class,
        HttpModule::class,
        ViewModelFactoryModule::class,
        ViewModelModule::class,
        WebViewModule::class
    ]
)
interface AppComponent {

    fun inject(app: DemoApplication)
}