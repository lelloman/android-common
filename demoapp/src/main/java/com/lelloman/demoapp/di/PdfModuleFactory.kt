package com.lelloman.demoapp.di

import com.lelloman.common.di.KoinModuleFactory
import com.lelloman.common.webview.interceptor.pdf.DefaultPdfUriOpener
import com.lelloman.common.webview.interceptor.pdf.PdfUriOpener
import org.koin.dsl.module

class PdfModuleFactory : KoinModuleFactory {

    override fun makeKoinModule(override: Boolean) = module(override = override) {
        factory<PdfUriOpener> {
            DefaultPdfUriOpener(context = get())
        }
    }
}