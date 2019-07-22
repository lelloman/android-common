package com.lelloman.common.webview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.webkit.*


class CookedWebView : WebView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    var listener: Listener? = null

    private val interceptors = linkedSetOf<CookedWebViewInterceptor>()

    private val webClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            listener?.onPageLoadingStateChanged(0)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            listener?.onPageLoadingStateChanged(100)
        }

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest): Boolean {
            synchronized(interceptors) {
                interceptors.forEach {
                    if (it.interceptUrlLoading(context, this@CookedWebView, request)) {
                        return true
                    }
                }
                listener?.onPageUrlChanged(request.url.toString())
                return false
            }
        }

        override fun shouldInterceptRequest(
            view: WebView?,
            webResourceRequest: WebResourceRequest
        ): WebResourceResponse? {
            var response: WebResourceResponse? = null
            synchronized(interceptors) {
                val iterator = interceptors.iterator()
                while (iterator.hasNext()) {
                    val interceptor = iterator.next()
                    val intercepted = interceptor.interceptRequest(context, this@CookedWebView, webResourceRequest)
                    if (intercepted != null) {
                        response = intercepted
                        break
                    }
                }
            }
            return response
        }
    }

    private val webChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            listener?.onPageLoadingStateChanged(newProgress)
        }
    }

    init {
        super.setWebChromeClient(webChromeClient)
        super.setWebViewClient(webClient)
        with(super.getSettings()) {
            @SuppressLint("SetJavaScriptEnabled")
            javaScriptEnabled = true
            loadWithOverviewMode = true
        }
    }

    override fun setWebViewClient(client: WebViewClient?) {
        throw IllegalAccessException("Mind your own WebViewClients.")
    }

    override fun getSettings(): WebSettings {
        throw IllegalAccessException("Mind your own Settings.")
    }

    override fun setWebChromeClient(client: WebChromeClient?) {
        throw IllegalAccessException("Mind your own WebChromeClients.")
    }

    fun addInterceptor(interceptor: CookedWebViewInterceptor) = synchronized(interceptors) {
        interceptors.add(interceptor)
    }

    fun removeInterceptor(interceptor: CookedWebViewInterceptor) = synchronized(interceptors) {
        interceptors.remove(interceptor)
    }

    fun addInterceptors(interceptors: List<CookedWebViewInterceptor>) = synchronized(this.interceptors) {
        this.interceptors.addAll(interceptors)
    }

    fun removeInterceptors(interceptors: List<CookedWebViewInterceptor>) = synchronized(this.interceptors) {
        this.interceptors.removeAll(interceptors)
    }

    interface Listener {
        fun onPageLoadingStateChanged(percent: Int)
        fun onPageUrlChanged(newUrl: String)
    }
}