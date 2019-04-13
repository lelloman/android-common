package com.lelloman.common.webview

import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView

interface CookedWebViewInterceptor {
    fun interceptUrlLoading(webView: WebView, webResourceRequest: WebResourceRequest): Boolean

    fun interceptRequest(webView: WebView, webResourceRequest: WebResourceRequest): WebResourceResponse?
}