package com.lelloman.demoapp.di

import com.lelloman.common.http.HttpClient
import com.lelloman.common.webview.interceptor.AdBlockInterceptor
import com.lelloman.common.webview.interceptor.PdfInterceptor
import dagger.Module
import dagger.Provides


@Module
class CookedWebViewModule {

    @Provides
    fun provideAdBlockInterceptor() = AdBlockInterceptor()

    @Provides
    fun providePdfInterceptor(
        httpClient: HttpClient
    ) = PdfInterceptor(
        httpClient = httpClient
    )
}