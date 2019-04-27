package com.lelloman.demoapp.di

import android.content.Context
import com.lelloman.common.http.HttpClient
import com.lelloman.common.webview.interceptor.AdBlockInterceptor
import com.lelloman.common.webview.interceptor.PdfInterceptor
import com.lelloman.demoapp.R
import dagger.Module
import dagger.Provides


@Module
class CookedWebViewModule {

    @Provides
    fun provideAdBlockInterceptor(
        context: Context
    ) = AdBlockInterceptor(
        blockedDomains = context.resources.getStringArray(R.array.blockedDomains).toSet()
    )

    @Provides
    fun providePdfInterceptor(
        httpClient: HttpClient
    ) = PdfInterceptor(
        httpClient = httpClient
    )
}