package com.lelloman.common.di

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import com.lelloman.common.LLContext
import com.lelloman.common.di.qualifiers.*
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
        loggerFactory: LoggerFactory,
        packageManager: PackageManager,
        @ApplicationPackageName packageName: String
    ) = NavigationRouter(
        loggerFactory = loggerFactory,
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
        actionTokenProvider: ActionTokenProvider
    ) = BaseViewModel.Dependencies(
        settings = baseApplicationSettings,
        resourceProvider = resourceProvider,
        actionTokenProvider = actionTokenProvider
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
    fun provideContentUriOpener(llContext: LLContext): ContentUriOpener = llContext

    @Provides
    @Singleton
    fun provideFileProvider(context: Context): FileProvider = FileProviderImpl(context)

    @Provides
    @Singleton
    fun provideLlContext(
        context: Context,
        timeProvider: TimeProvider,
        @UiScheduler uiScheduler: Scheduler,
        @IoScheduler ioScheduler: Scheduler,
        @NewThreadScheduler newThreadScheduler: Scheduler,
        loggerFactory: LoggerFactory,
        meteredConnectionChecker: MeteredConnectionChecker,
        urlValidator: UrlValidator,
        picassoWrap: PicassoWrap,
        semanticTimeProvider: SemanticTimeProvider
    ) = LLContext(
        context = context,
        timeProvider = timeProvider,
        ioScheduler = ioScheduler,
        uiScheduler = uiScheduler,
        newThreadScheduler = newThreadScheduler,
        loggerFactory = loggerFactory,
        meteredConnectionChecker = meteredConnectionChecker,
        urlValidator = urlValidator,
        picassoWrap = picassoWrap,
        semanticTimeProvider = semanticTimeProvider
    )
}