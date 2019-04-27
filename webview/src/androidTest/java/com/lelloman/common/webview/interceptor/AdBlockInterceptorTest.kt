package com.lelloman.common.webview.interceptor

import android.content.Context
import android.net.Uri
import android.webkit.WebResourceRequest
import com.lelloman.common.webview.CookedWebView
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AdBlockInterceptorTest {

    private val context: Context = mock()
    private val webView: CookedWebView = mock()

    @Test
    fun returnsEmptyResponseForRequestToExactlyBlockedDomain() {
        val tested = AdBlockInterceptor(setOf("asd.com"))

        val intercepted = tested.interceptRequest(context, webView, makeWebResourceRequest("http://asd.com"))

        assertThat(intercepted).isNotNull
        with(intercepted!!) {
            assertThat(mimeType).isEqualTo("text/plain")
            assertThat(statusCode).isEqualTo(200)
            assertThat(data.bufferedReader().readText()).hasSize(0)
        }
        verifyZeroInteractions(context, webView)
    }

    @Test
    fun returnsEmptyResponseForRequestToExactlyBlockedSecondLevelDomain() {
        val tested = AdBlockInterceptor(setOf("asdasd.asd.com"))

        val intercepted = tested.interceptRequest(context, webView, makeWebResourceRequest("http://asdasd.asd.com"))

        assertThat(intercepted).isNotNull
        with(intercepted!!) {
            assertThat(mimeType).isEqualTo("text/plain")
            assertThat(statusCode).isEqualTo(200)
            assertThat(data.bufferedReader().readText()).hasSize(0)
        }
        verifyZeroInteractions(context, webView)
    }

    @Test
    fun returnsEmptyResponseForRequestToBlockedDomainWithPath() {
        val tested = AdBlockInterceptor(setOf("asd.com"))

        val intercepted =
            tested.interceptRequest(context, webView, makeWebResourceRequest("http://asd.com/signor/gargiulo"))

        assertThat(intercepted).isNotNull
        with(intercepted!!) {
            assertThat(mimeType).isEqualTo("text/plain")
            assertThat(statusCode).isEqualTo(200)
            assertThat(data.bufferedReader().readText()).hasSize(0)
        }
        verifyZeroInteractions(context, webView)
    }

    @Test
    fun returnsNullForRequestToUnblockedSubdomain() {
        val tested = AdBlockInterceptor(setOf("asd.com"))

        val intercepted = tested.interceptRequest(context, webView, makeWebResourceRequest("https://asdasd.asd.com"))

        assertThat(intercepted).isNull()
    }

    private fun makeWebResourceRequest(url: String): WebResourceRequest = mock {
        on { getUrl() }.thenReturn(Uri.parse(url))
    }
}