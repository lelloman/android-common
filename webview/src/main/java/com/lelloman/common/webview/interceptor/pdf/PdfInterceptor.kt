package com.lelloman.common.webview.interceptor.pdf

import android.content.Context
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import com.lelloman.common.http.ApplicationContentType
import com.lelloman.common.http.ContentType
import com.lelloman.common.http.HttpClient
import com.lelloman.common.http.HttpResponse
import com.lelloman.common.http.request.HttpRequest
import com.lelloman.common.http.request.HttpRequestMethod
import com.lelloman.common.webview.CookedWebView
import com.lelloman.common.webview.CookedWebViewInterceptor

class PdfInterceptor(
    private val httpClient: HttpClient,
    private val pdfUriOpener: PdfUriOpener
) : CookedWebViewInterceptor {

    private var lastLoadedUrl: String? = null

    override fun interceptUrlLoading(
        context: Context,
        webView: CookedWebView,
        webResourceRequest: WebResourceRequest
    ): Boolean {
        lastLoadedUrl = webResourceRequest.url.toString()
        return false
    }

    override fun interceptRequest(
        context: Context,
        webView: CookedWebView,
        webResourceRequest: WebResourceRequest
    ): WebResourceResponse? {
        val url = webResourceRequest.url.toString()
        if (url == lastLoadedUrl && webResourceRequest.method == "GET") {
            val httpResponse = HttpRequest(url = url, method = HttpRequestMethod.HEAD)
                .let(httpClient::request)
                .onErrorReturn { HttpResponse(0, false) }
                .blockingGet()

            if (httpResponse.isSuccessful && httpResponse.contentType.isPdf()) {
                pdfUriOpener.openPdfUri(webResourceRequest.url.toString())
            }
        }

        return null
    }

    private fun ContentType.isPdf() = this is ApplicationContentType && this.subType in arrayOf("pdf", "x-pdf")
}