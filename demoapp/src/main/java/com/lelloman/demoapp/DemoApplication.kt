package com.lelloman.demoapp

import com.lelloman.common.BaseApplication
import com.lelloman.common.http.HttpModuleFactory
import com.lelloman.demoapp.di.CookedWebViewModuleFactory
import com.lelloman.demoapp.di.PdfModuleFactory
import com.lelloman.demoapp.di.ViewModelModuleFactory

open class DemoApplication : BaseApplication() {

    override fun getKoinModuleFactories() = listOf(
        PdfModuleFactory(),
        ViewModelModuleFactory(),
        HttpModuleFactory(),
        CookedWebViewModuleFactory()
    )
}