package com.lelloman.common.http

import com.lelloman.common.di.KoinModuleFactory
import com.lelloman.common.http.internal.HttpClientImpl
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.utils.TimeProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import okhttp3.CookieJar
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import org.koin.dsl.module
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.Executors

open class HttpModule : KoinModuleFactory {
    override fun makeKoinModule() = module {

        single {
            provideCookieJar()
        }

        single {
            provideOkHttpClient(cookieJar = get())
        }

        single {
            provideHttpClient(
                okHttpClient = get(),
                loggerFactory = get(),
                timeProvider = get()
            )
        }

        single(HttpPoolScheduler) {
            provideHttpPoolScheduler()
        }
    }

    open fun provideCookieJar(): CookieJar = CookieManager()
        .apply { setCookiePolicy(CookiePolicy.ACCEPT_ALL) }
        .let(::JavaNetCookieJar)

    open fun provideOkHttpClient(cookieJar: CookieJar): OkHttpClient =
        OkHttpClient
            .Builder()
            .cookieJar(cookieJar)
            .build()

    open fun provideHttpClient(
        okHttpClient: OkHttpClient,
        loggerFactory: LoggerFactory,
        timeProvider: TimeProvider
    ): HttpClient = HttpClientImpl(
        okHttpClient = okHttpClient,
        loggerFactory = loggerFactory,
        timeProvider = timeProvider
    )

    open fun provideHttpPoolScheduler(): Scheduler = Executors
        .newFixedThreadPool(5)
        .let(Schedulers::from)
}