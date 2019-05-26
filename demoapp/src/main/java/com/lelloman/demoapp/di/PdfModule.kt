package com.lelloman.demoapp.di

import android.content.Context
import com.lelloman.common.webview.interceptor.pdf.DefaultPdfUriOpener
import com.lelloman.common.webview.interceptor.pdf.PdfUriOpener
import dagger.Module
import dagger.Provides

@Module
class PdfModule {

    @Provides
    fun provideDefaultPdfUriOpener(context: Context): PdfUriOpener = DefaultPdfUriOpener(context)
}