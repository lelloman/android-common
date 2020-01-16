package com.lelloman.demoapp.ui.webview

import com.lelloman.common.utils.BooleanLiveData
import com.lelloman.common.utils.StringLiveData
import com.lelloman.common.utils.immutable
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.common.webview.CookedWebView
import com.lelloman.common.webview.interceptor.AdBlockInterceptor
import com.lelloman.common.webview.interceptor.pdf.PdfInterceptor

class WebViewViewModel(
    dependencies: Dependencies,
    pdfInterceptor: PdfInterceptor,
    adBlockInterceptor: AdBlockInterceptor
) : BaseViewModel(dependencies), CookedWebView.Listener {

    private val mutableProgressVisible = BooleanLiveData()
    val progressVisible = mutableProgressVisible.immutable

    private val mutableCurrentUrl = StringLiveData()
    val currentUrl = mutableCurrentUrl.immutable

    val interceptors = listOf(pdfInterceptor, adBlockInterceptor)

    private val mutableAddress = StringLiveData().apply {
        value = "https://www.imslp.org".apply { mutableCurrentUrl.postValue(this) }
//        value = "https://www.orimi.com/pdf-test.pdf"
//        value = "https://imslp.org/wiki/Special:IMSLPDisclaimerAccept/548820"
    }
    val address = mutableAddress.immutable

    override fun onPageLoadingStateChanged(percent: Int) {
        mutableProgressVisible.postValue(percent != 100)
    }

    override fun onPageUrlChanged(newUrl: String) {
        mutableCurrentUrl.postValue(newUrl)
    }
}