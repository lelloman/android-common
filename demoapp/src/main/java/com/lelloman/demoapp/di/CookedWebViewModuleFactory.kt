package com.lelloman.demoapp.di

import android.content.Context
import com.lelloman.common.di.KoinModuleFactory
import com.lelloman.common.webview.interceptor.AdBlockInterceptor
import com.lelloman.common.webview.interceptor.pdf.PdfInterceptor
import com.lelloman.demoapp.R
import org.koin.dsl.module

class CookedWebViewModuleFactory : KoinModuleFactory {

    override fun makeKoinModule(override: Boolean) = module(override = override) {
        factory {
            AdBlockInterceptor(
                blockedDomains = get<Context>().resources.getStringArray(R.array.blockedDomains).toSet()
            )
        }
        factory {
            PdfInterceptor(
                httpClient = get(),
                pdfUriOpener = get()
            )
        }
    }
}