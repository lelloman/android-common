package com.lelloman.common.webview.interceptor

import android.content.Context
import android.net.Uri
import android.webkit.WebResourceRequest
import com.lelloman.common.http.ApplicationContentType
import com.lelloman.common.http.HttpClient
import com.lelloman.common.http.HttpResponse
import com.lelloman.common.http.request.HttpRequest
import com.lelloman.common.http.request.HttpRequestMethod
import com.lelloman.common.webview.CookedWebView
import com.lelloman.common.webview.interceptor.pdf.PdfInterceptor
import com.lelloman.common.webview.interceptor.pdf.PdfUriOpener
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PdfInterceptorTest {

    private val context: Context = mock()
    private val webView: CookedWebView = mock()
    private val httpClient: HttpClient = mock {
        on { request(any()) }.thenReturn(Single.error(Exception()))
    }
    private val pdfUriOpener: PdfUriOpener = mock()

    private val tested = PdfInterceptor(
        httpClient = httpClient,
        pdfUriOpener = pdfUriOpener
    )

    @Test
    fun doesNotMakeHeadRequestIfNoUrlHasBeenLoadedBefore() {
        val request: WebResourceRequest =
            mock { on { url }.thenReturn(Uri.parse("http://www.asd.com")) }

        val intercepted = tested.interceptRequest(context, webView, request)

        verify(request).url
        verifyNoMoreInteractions(request)
        verifyZeroInteractions(httpClient)
        assertThat(intercepted).isNull()
    }

    @Test
    fun doesNotMakeHeadRequestIfLoadedUrlIsDifferentFromRequest() {
        val urlLoadingRequest = makeWebRequest("http://www.someurl.com")
        assertThat(tested.interceptUrlLoading(context, webView, urlLoadingRequest)).isFalse()
        val request: WebResourceRequest = makeWebRequest("http://www.someotherurl.com")

        val intercepted = tested.interceptRequest(context, webView, request)

        verify(request).url
        verifyNoMoreInteractions(request)
        verifyZeroInteractions(webView, httpClient)
        assertThat(intercepted).isNull()
    }

    @Test
    fun doesMakeHeadRequestIfRequestUrlIsLoadedUrlAndMethodIsGet() {
        val requestUrl = "http://isthisapdf.com"
        val request = makeWebRequest(requestUrl, "GET")
        tested.interceptUrlLoading(context, webView, request)

        val intercepted = tested.interceptRequest(context, webView, request)

        verify(httpClient).request(HttpRequest(url = requestUrl, method = HttpRequestMethod.HEAD))
        assertThat(intercepted).isNull()
    }

    @Test
    fun doesNotMakeHeadRequestIfRequestUrlIsLoadedUrlAndMethodIsPost() {
        val requestUrl = "http://isthisapdf.com"
        val request = makeWebRequest(requestUrl, "POST")
        tested.interceptUrlLoading(context, webView, request)

        val intercepted = tested.interceptRequest(context, webView, request)

        verify(request, times(2)).url
        verify(request).method
        verifyNoMoreInteractions(request)
        verifyZeroInteractions(webView, httpClient)
        assertThat(intercepted).isNull()
    }

    @Test
    fun startsPdfViewIntentWhenRequestedUrlHasPdfContentType() {
        val requestUrl = "http://www.definitelyapdf.com"
        val request = makeWebRequest(requestUrl)
        val httpResponse = HttpResponse(200, true, contentType = ApplicationContentType("pdf"))
        whenever(httpClient.request(any())).thenReturn(Single.just(httpResponse))
        tested.interceptUrlLoading(context, webView, request)

        val intercepted = tested.interceptRequest(context, webView, request)

        assertThat(intercepted).isNull()
        verify(pdfUriOpener).openPdfUri(requestUrl)
    }

    private fun makeWebRequest(
        requestUrl: String,
        requestMethod: String = "GET"
    ): WebResourceRequest = mock {
        on { url }.thenReturn(Uri.parse(requestUrl))
        on { method }.thenReturn(requestMethod)
    }
}