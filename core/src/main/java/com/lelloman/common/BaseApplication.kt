package com.lelloman.common

import android.app.Application
import android.content.Context
import com.lelloman.common.di.BaseApplicationModuleFactory
import com.lelloman.common.di.KoinModuleFactory
import com.lelloman.common.settings.BaseSettingsModuleFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

abstract class BaseApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        inject()
    }

    protected open fun makeBaseApplicationModule() = BaseApplicationModuleFactory()

    protected open fun makeBaseSettingsModule() = BaseSettingsModuleFactory()

    protected open fun getKoinModuleFactories() = mutableListOf<KoinModuleFactory>()

    private fun getBaseKoinModuleFactories() = mutableListOf(
        makeBaseApplicationModule(),
        makeBaseSettingsModule()
    )

    protected open fun inject() {
        val moduleFactories = getBaseKoinModuleFactories().apply {
            addAll(getKoinModuleFactories())
        }
        startKoin {
            androidContext(this@BaseApplication)
            modules(moduleFactories.map(KoinModuleFactory::makeKoinModule))
        }
    }
}