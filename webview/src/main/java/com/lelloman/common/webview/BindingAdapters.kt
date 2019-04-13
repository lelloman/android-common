package com.lelloman.common.webview

import androidx.databinding.BindingAdapter

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("listener")
    fun bindListener(cookedWebView: CookedWebView, listener: CookedWebView.Listener) {
        cookedWebView.listener = listener
    }
}