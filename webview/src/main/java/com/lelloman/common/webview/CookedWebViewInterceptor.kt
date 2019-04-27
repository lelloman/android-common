package com.lelloman.common.webview

import android.content.Context
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse

interface CookedWebViewInterceptor {
    fun interceptUrlLoading(context: Context, webView: CookedWebView, webResourceRequest: WebResourceRequest): Boolean

    fun interceptRequest(
        context: Context,
        webView: CookedWebView,
        webResourceRequest: WebResourceRequest
    ): WebResourceResponse?
}