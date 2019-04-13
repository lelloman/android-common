package com.lelloman.common.http

import com.lelloman.common.http.internal.HttpClientImpl
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.utils.TimeProvider
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import okhttp3.CookieJar
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
open class HttpModule {

    @Provides
    @Singleton
    fun provideCookieJar(): CookieJar = CookieManager()
        .apply { setCookiePolicy(CookiePolicy.ACCEPT_ALL) }
        .let(::JavaNetCookieJar)

    @Singleton
    @Provides
    open fun provideOkHttpClient(cookieJar: CookieJar): OkHttpClient =
        OkHttpClient
            .Builder()
            .cookieJar(cookieJar)
            .build()

    @Singleton
    @Provides
    open fun provideHttpClient(
        okHttpClient: OkHttpClient,
        loggerFactory: LoggerFactory,
        timeProvider: TimeProvider
    ): HttpClient = HttpClientImpl(
        okHttpClient = okHttpClient,
        loggerFactory = loggerFactory,
        timeProvider = timeProvider
    )

    @Singleton
    @Provides
    @HttpPoolScheduler
    open fun provideHttpPoolScheduler(): Scheduler = Executors
        .newFixedThreadPool(5)
        .let(Schedulers::from)
}