package com.lelloman.demoapp.ui.webview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.common.webview.CookedWebView
import com.lelloman.common.webview.interceptor.AdBlockInterceptor
import com.lelloman.common.webview.interceptor.pdf.PdfInterceptor

class WebViewViewModel(
    dependencies: Dependencies,
    pdfInterceptor: PdfInterceptor,
    adBlockInterceptor: AdBlockInterceptor
) : BaseViewModel(dependencies), CookedWebView.Listener {

    private val mutableProgressVisible = MutableLiveData<Boolean>()
    val progressVisible: LiveData<Boolean> = mutableProgressVisible

    val interceptors = listOf(pdfInterceptor, adBlockInterceptor)

    private val mutableUrl = MutableLiveData<String>().apply {
        value = "https://www.imslp.org"
//        value = "https://www.orimi.com/pdf-test.pdf"
//        value = "https://imslp.org/wiki/Special:IMSLPDisclaimerAccept/548820"
    }
    val url: LiveData<String> = mutableUrl

    override fun onPageLoadingStateChanged(percent: Int) {
        mutableProgressVisible.postValue(percent != 100)
    }
}