package com.lelloman.common.settings

import android.content.Context
import com.lelloman.common.di.KoinModuleFactory
import com.lelloman.common.di.qualifiers.DefaultAppTheme
import com.lelloman.common.view.AppTheme
import org.koin.dsl.module

open class BaseSettingsModuleFactory : KoinModuleFactory {

    override fun makeKoinModule(override: Boolean) = module(override = override) {
        single {
            provideBaseApplicationSettings(
                context = get(),
                defaultAppTheme = get(DefaultAppTheme)
            )
        }
    }

    open fun provideBaseApplicationSettings(
        context: Context,
        defaultAppTheme: AppTheme
    ): BaseApplicationSettings = BaseApplicationSettingsImpl(
        context = context,
        defaultAppTheme = defaultAppTheme
    )
}