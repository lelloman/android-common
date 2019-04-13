package com.lelloman.common.webview.interceptor

import android.content.Intent
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import com.lelloman.common.http.ApplicationContentType
import com.lelloman.common.http.ContentType
import com.lelloman.common.http.HttpClient
import com.lelloman.common.http.HttpResponse
import com.lelloman.common.http.request.HttpRequest
import com.lelloman.common.http.request.HttpRequestMethod
import com.lelloman.common.webview.CookedWebViewInterceptor

class PdfInterceptor(private val httpClient: HttpClient) : CookedWebViewInterceptor {

    override fun interceptUrlLoading(webView: WebView, webResourceRequest: WebResourceRequest) = false

    override fun interceptRequest(webView: WebView, webResourceRequest: WebResourceRequest): WebResourceResponse? {
        if (webResourceRequest.method == "GET") {
            val httpResponse = HttpRequest(url = webResourceRequest.url.toString(), method = HttpRequestMethod.HEAD)
                .let(httpClient::request)
                .onErrorReturn { HttpResponse(0, false) }
                .blockingGet()

            if (httpResponse.isSuccessful && httpResponse.contentType.isPdf()) {
                val intent = Intent(Intent.ACTION_VIEW).setDataAndType(webResourceRequest.url, "application/pdf")
                webView.context.startActivity(intent)
            }
        }

        return null
    }

    private fun ContentType.isPdf() = this is ApplicationContentType && this.subType in arrayOf("pdf", "x-pdf")
}