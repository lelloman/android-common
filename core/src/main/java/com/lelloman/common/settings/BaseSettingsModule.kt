package com.lelloman.common.settings

import android.content.Context
import com.lelloman.common.di.qualifiers.DefaultAppTheme
import com.lelloman.common.view.AppTheme
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class BaseSettingsModule {

    @Singleton
    @Provides
    open fun provideBaseApplicationSettings(
        context: Context,
        @DefaultAppTheme defaultAppTheme: AppTheme
    ): BaseApplicationSettings = BaseApplicationSettingsImpl(
        context = context,
        defaultAppTheme = defaultAppTheme
    )
}