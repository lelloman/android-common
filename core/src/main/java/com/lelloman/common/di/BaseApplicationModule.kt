package com.lelloman.common.di

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import com.lelloman.common.di.qualifiers.ApplicationPackageName
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.di.qualifiers.NewThreadScheduler
import com.lelloman.common.di.qualifiers.UiScheduler
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.logger.LoggerFactoryImpl
import com.lelloman.common.navigation.NavigationRouter
import com.lelloman.common.settings.BaseApplicationSettings
import com.lelloman.common.utils.*
import com.lelloman.common.view.*
import com.lelloman.common.viewmodel.BaseViewModel
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
open class BaseApplicationModule(private val application: Application) {

    @Provides
    fun provideContext(): Context = application

    @Provides
    @Singleton
    fun provideApplicationInfoProvider(
        context: Context,
        packageManager: PackageManager
    ) : ApplicationInfoProvider = ApplicationInfoProviderImpl(
        context = context,
        packageManager = packageManager
    )

    @Singleton
    @Provides
    @IoScheduler
    open fun provideIoScheduler(): Scheduler = Schedulers.io()

    @Singleton
    @Provides
    @UiScheduler
    open fun provideUiScheduler(): Scheduler = AndroidSchedulers.mainThread()

    @Singleton
    @Provides
    @NewThreadScheduler
    open fun provideNewThreadScheduler(): Scheduler = Schedulers.newThread()

    @Singleton
    @Provides
    open fun provideLoggerFactory(): LoggerFactory = LoggerFactoryImpl()

    @Singleton
    @Provides
    fun provideTimeProvider(): TimeProvider = TimeProviderImpl()

    @Singleton
    @Provides
    fun provideResourceProvider(context: Context): ResourceProvider = ResourceProviderImpl(context)

    @Provides
    open fun providePackageManager(context: Context): PackageManager = context.packageManager

    @Provides
    fun provideNavigationRouter(
        packageManager: PackageManager,
        @ApplicationPackageName packageName: String
    ) = NavigationRouter(
        packageManager = packageManager,
        applicationPackageName = packageName
    )

    @Singleton
    @Provides
    open fun provideMeteredConnectionChecker(context: Context): MeteredConnectionChecker = MeteredConnectionCheckerImpl(context)

    @Singleton
    @Provides
    fun provideActionTokenProvider() = ActionTokenProvider()

    @Singleton
    @Provides
    fun provideUrlValidator(): UrlValidator = UrlValidatorImpl()

    @Singleton
    @Provides
    fun providePicassoWrap(
        appSettings: BaseApplicationSettings,
        meteredConnectionChecker: MeteredConnectionChecker
    ): PicassoWrap = PicassoWrapImpl(
        useMeteredNetwork = appSettings.useMeteredNetwork,
        meteredConnectionChecker = meteredConnectionChecker
    )

    @Singleton
    @Provides
    fun provideSemanticTimeProvider(
        timeProvider: TimeProvider,
        resourceProvider: ResourceProvider
    ): SemanticTimeProvider = SemanticTimeProviderImpl(
        timeProvider = timeProvider,
        resourceProvider = resourceProvider
    )

    @Provides
    fun provideBaseViewModelDependencies(
        baseApplicationSettings: BaseApplicationSettings,
        resourceProvider: ResourceProvider,
        actionTokenProvider: ActionTokenProvider,
        @UiScheduler uiScheduler: Scheduler,
        @IoScheduler ioScheduler: Scheduler,
        loggerFactory: LoggerFactory
    ) = BaseViewModel.Dependencies(
        settings = baseApplicationSettings,
        resourceProvider = resourceProvider,
        actionTokenProvider = actionTokenProvider,
        uiScheduler = uiScheduler,
        ioScheduler = ioScheduler,
        loggerFactory = loggerFactory
    )

    @Provides
    fun provideBroadcastReceiverWrap(context: Context) =
        BroadcastReceiverWrap(context)

    @Provides
    @ApplicationPackageName
    fun provideApplicationPackageName(infoProvider: ApplicationInfoProvider): String = infoProvider.packageName

    @Provides
    @Singleton
    fun provideAssetManager(context: Context): AssetManager = context.assets

    @Provides
    @Singleton
    fun provideContentUriOpener(context: Context): ContentUriOpener = ContentUriOpenerImpl(context)

    @Provides
    @Singleton
    fun provideFileProvider(context: Context): FileProvider = FileProviderImpl(context)
}