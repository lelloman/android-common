package com.lelloman.common.webview.interceptor

import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import com.lelloman.common.webview.CookedWebViewInterceptor
import java.io.ByteArrayInputStream

class AdBlockInterceptor : CookedWebViewInterceptor {
    override fun interceptUrlLoading(webView: WebView, webResourceRequest: WebResourceRequest): Boolean = false

    override fun interceptRequest(webView: WebView, webResourceRequest: WebResourceRequest): WebResourceResponse? {
        return if (BLOCKED_DOMAINS.contains(webResourceRequest.url.host ?: "")) {
            WebResourceResponse("text/plain", "utf-8", ByteArrayInputStream(ByteArray(0)))
        } else {
            null
        }
    }

    private companion object {
        val BLOCKED_DOMAINS = setOf(
            "adservice.google.com",
            "adservice.google.nl",
            "googleads.g.doubleclick.net",
            "pagead2.googlesyndication.com",
            "pixel.quantserve.com",
            "pro.ip-api.com",
            "rules.quantcount.com",
            "secure.quantserve.com",
            "tpc.googlesyndication.com",
            "www.googletagservices.com"
        )
    }
}