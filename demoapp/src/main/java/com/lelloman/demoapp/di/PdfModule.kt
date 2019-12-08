package com.lelloman.demoapp.di

import com.lelloman.common.di.KoinModuleFactory
import com.lelloman.common.webview.interceptor.pdf.DefaultPdfUriOpener
import com.lelloman.common.webview.interceptor.pdf.PdfUriOpener
import org.koin.dsl.module

class PdfModule : KoinModuleFactory {

    override fun makeKoinModule() = module {
        factory<PdfUriOpener> {
            DefaultPdfUriOpener(context = get())
        }
    }
}