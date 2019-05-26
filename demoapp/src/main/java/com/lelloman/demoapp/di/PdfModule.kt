package com.lelloman.demoapp.di

import com.lelloman.common.webview.interceptor.pdf.DefaultPdfUriOpener
import com.lelloman.common.webview.interceptor.pdf.PdfUriOpener
import dagger.Module
import dagger.Provides

@Module
class PdfModule {

    @Provides
    fun provideDefaultPdfUriOpener(): PdfUriOpener = DefaultPdfUriOpener()
}