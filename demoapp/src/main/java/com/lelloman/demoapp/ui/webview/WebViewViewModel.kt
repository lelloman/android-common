package com.lelloman.demoapp.ui.webview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.common.webview.CookedWebView

class WebViewViewModel(dependencies: Dependencies) : BaseViewModel(dependencies), CookedWebView.Listener {

    private val mutableProgressVisible = MutableLiveData<Boolean>()
    val progressVisible: LiveData<Boolean> = mutableProgressVisible

    override fun onPageLoadingStateChanged(percent: Int) {
        mutableProgressVisible.postValue(percent != 100)
    }
}