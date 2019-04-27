package com.lelloman.common.webview.interceptor

import android.content.Context
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import com.lelloman.common.webview.CookedWebView
import com.lelloman.common.webview.CookedWebViewInterceptor
import java.io.ByteArrayInputStream

class AdBlockInterceptor(private val blockedDomains: Set<String>) : CookedWebViewInterceptor {

    override fun interceptUrlLoading(
        context: Context,
        webView: CookedWebView,
        webResourceRequest: WebResourceRequest
    ): Boolean = false

    override fun interceptRequest(
        context: Context,
        webView: CookedWebView,
        webResourceRequest: WebResourceRequest
    ): WebResourceResponse? = if (blockedDomains.contains(webResourceRequest.url.host ?: "")) {
        WebResourceResponse("text/plain", "utf-8", 200, "OK", emptyMap(), ByteArrayInputStream(ByteArray(0)))
    } else {
        null
    }
}