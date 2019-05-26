package com.lelloman.common.webview

import androidx.databinding.BindingAdapter

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("url")
    fun bindUrl(cookedWebView: CookedWebView, url: String) {
        cookedWebView.loadUrl(url)
    }

    @JvmStatic
    @BindingAdapter("interceptors")
    fun bindInterceptors(cookedWebView: CookedWebView, interceptors: List<CookedWebViewInterceptor>) {
        cookedWebView.addInterceptors(interceptors)
    }
}