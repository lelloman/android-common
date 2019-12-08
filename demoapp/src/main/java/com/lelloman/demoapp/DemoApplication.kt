package com.lelloman.demoapp

import com.lelloman.common.BaseApplication
import com.lelloman.common.di.KoinModuleFactory
import com.lelloman.common.http.HttpModule
import com.lelloman.demoapp.di.CookedWebViewModule
import com.lelloman.demoapp.di.PdfModule
import com.lelloman.demoapp.di.ViewModelModule

open class DemoApplication : BaseApplication() {

    override fun getKoinModuleFactories(): MutableList<KoinModuleFactory> {
        return super.getKoinModuleFactories().apply {
            add(PdfModule())
            add(ViewModelModule())
            add(HttpModule())
            add(CookedWebViewModule())
        }
    }
}