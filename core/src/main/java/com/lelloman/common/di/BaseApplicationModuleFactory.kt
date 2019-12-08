package com.lelloman.common.di

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import com.lelloman.common.di.qualifiers.*
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.logger.LoggerFactoryImpl
import com.lelloman.common.navigation.NavigationRouter
import com.lelloman.common.settings.BaseApplicationSettings
import com.lelloman.common.utils.*
import com.lelloman.common.view.*
import com.lelloman.common.viewmodel.BaseViewModel
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.dsl.module

open class BaseApplicationModuleFactory : KoinModuleFactory {

    override fun makeKoinModule() = module {
        single {
            provideApplicationInfoProvider(
                context = get(),
                packageManager = get()
            )
        }
        single(IoScheduler) {
            provideIoScheduler()
        }
        single(UiScheduler) {
            provideUiScheduler()
        }
        single(NewThreadScheduler) {
            provideNewThreadScheduler()
        }
        single {
            provideLoggerFactory()
        }
        single {
            provideTimeProvider()
        }
        single {
            provideResourceProvider(context = get())
        }
        single {
            providePackageManager(context = get())
        }
        single {
            provideNavigationRouter(
                packageManager = get(),
                applicationPackageName = get(ApplicationPackageName)
            )
        }
        single {
            provideMeteredConnectionChecker(context = get())
        }
        single {
            provideActionTokenProvider()
        }
        single {
            provideUrlValidator()
        }
        single {
            providePicassoWrap(
                appSettings = get(),
                meteredConnectionChecker = get()
            )
        }
        single {
            provideSemanticTimeProvider(
                timeProvider = get(),
                resourceProvider = get()
            )
        }
        single {
            provideBaseViewModelDependencies(
                baseApplicationSettings = get(),
                resourceProvider = get(),
                actionTokenProvider = get(),
                uiScheduler = get(UiScheduler),
                ioScheduler = get(IoScheduler),
                loggerFactory = get()
            )
        }
        single {
            provideBroadcastReceiverWrap(context = get())
        }
        single(ApplicationPackageName) {
            provideApplicationPackageName(infoProvider = get())
        }
        single {
            provideAssetManager(context = get())
        }
        single {
            provideContentUriOpener(context = get())
        }
        single {
            provideFileProvider(context = get())
        }
        single(DefaultAppTheme) {
            provideDefaultAppTheme()
        }
    }

    open fun provideApplicationInfoProvider(
        context: Context,
        packageManager: PackageManager
    ): ApplicationInfoProvider = ApplicationInfoProviderImpl(
        context = context,
        packageManager = packageManager
    )

    open fun provideIoScheduler(): Scheduler = Schedulers.io()

    open fun provideUiScheduler(): Scheduler = AndroidSchedulers.mainThread()

    open fun provideNewThreadScheduler(): Scheduler = Schedulers.newThread()

    open fun provideLoggerFactory(): LoggerFactory = LoggerFactoryImpl()

    open fun provideTimeProvider(): TimeProvider = TimeProviderImpl()

    open fun provideResourceProvider(context: Context): ResourceProvider =
        ResourceProviderImpl(context)

    open fun providePackageManager(context: Context): PackageManager = context.packageManager

    open fun provideNavigationRouter(
        packageManager: PackageManager,
        applicationPackageName: String
    ) = NavigationRouter(
        packageManager = packageManager,
        applicationPackageName = applicationPackageName
    )

    open fun provideMeteredConnectionChecker(context: Context): MeteredConnectionChecker =
        MeteredConnectionCheckerImpl(context)

    open fun provideActionTokenProvider() = ActionTokenProvider()

    open fun provideUrlValidator(): UrlValidator = UrlValidatorImpl()

    open fun providePicassoWrap(
        appSettings: BaseApplicationSettings,
        meteredConnectionChecker: MeteredConnectionChecker
    ): PicassoWrap = PicassoWrapImpl(
        useMeteredNetwork = appSettings.useMeteredNetwork,
        meteredConnectionChecker = meteredConnectionChecker
    )

    open fun provideSemanticTimeProvider(
        timeProvider: TimeProvider,
        resourceProvider: ResourceProvider
    ): SemanticTimeProvider = SemanticTimeProviderImpl(
        timeProvider = timeProvider,
        resourceProvider = resourceProvider
    )

    open fun provideBaseViewModelDependencies(
        baseApplicationSettings: BaseApplicationSettings,
        resourceProvider: ResourceProvider,
        actionTokenProvider: ActionTokenProvider,
        uiScheduler: Scheduler,
        ioScheduler: Scheduler,
        loggerFactory: LoggerFactory
    ) = BaseViewModel.Dependencies(
        settings = baseApplicationSettings,
        resourceProvider = resourceProvider,
        actionTokenProvider = actionTokenProvider,
        uiScheduler = uiScheduler,
        ioScheduler = ioScheduler,
        loggerFactory = loggerFactory
    )

    open fun provideBroadcastReceiverWrap(context: Context) =
        BroadcastReceiverWrap(context)

    open fun provideApplicationPackageName(infoProvider: ApplicationInfoProvider): String =
        infoProvider.packageName

    open fun provideAssetManager(context: Context): AssetManager = context.assets

    open fun provideContentUriOpener(context: Context): ContentUriOpener =
        ContentUriOpenerImpl(context)

    open fun provideFileProvider(context: Context): FileProvider = FileProviderImpl(context)

    open fun provideDefaultAppTheme() = AppTheme.DEFAULT
}