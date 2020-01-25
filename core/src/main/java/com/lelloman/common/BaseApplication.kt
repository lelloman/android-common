package com.lelloman.common

import android.app.Application
import android.content.Context
import com.lelloman.common.data.settings.BaseSettingsModuleFactory
import com.lelloman.common.di.BaseApplicationModuleFactory
import com.lelloman.common.di.KoinModuleFactory
import com.lelloman.common.view.AppTheme
import com.lelloman.common.view.AppThemes
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

abstract class BaseApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        AppThemes.addThemes(getThemes())
        val moduleFactories = getBaseKoinModuleFactories().apply {
            addAll(getKoinModuleFactories())
        }
        startKoin {
            androidContext(this@BaseApplication)
            modules(moduleFactories.map { it.makeKoinModule() })
        }
        inject()
    }

    protected open fun getThemes() = mutableSetOf(
        AppTheme("Light" , R.style.CustomTheme_Light, true),
        AppTheme("Darcula" , R.style.CustomTheme_Darcula, false),
        AppTheme("Mockito" , R.style.CustomTheme_Mockito, true),
        AppTheme("Black" , R.style.CustomTheme_Black, false),
        AppTheme("Forest" , R.style.CustomTheme_Forest, true)
    )

    protected open fun makeBaseApplicationModule() = BaseApplicationModuleFactory()

    protected open fun makeBaseSettingsModule() = BaseSettingsModuleFactory()

    protected open fun getKoinModuleFactories(): List<KoinModuleFactory> = mutableListOf()

    private fun getBaseKoinModuleFactories() = mutableListOf(
        makeBaseApplicationModule(),
        makeBaseSettingsModule()
    )

    protected open fun inject() = Unit
}