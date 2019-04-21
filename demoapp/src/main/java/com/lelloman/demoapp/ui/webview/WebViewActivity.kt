package com.lelloman.demoapp.ui.webview

import android.content.Context
import android.content.Intent
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.DeepLinkStartable
import com.lelloman.common.view.BaseActivity
import com.lelloman.common.webview.interceptor.AdBlockInterceptor
import com.lelloman.common.webview.interceptor.PdfInterceptor
import com.lelloman.demoapp.R
import com.lelloman.demoapp.databinding.ActivityWebViewBinding
import javax.inject.Inject

class WebViewActivity : BaseActivity<WebViewViewModel, ActivityWebViewBinding>() {

    override val layoutResId = R.layout.activity_web_view

    @Inject
    lateinit var adBlockInterceptor: AdBlockInterceptor

    @Inject
    lateinit var pdfInterceptor: PdfInterceptor

    override fun setViewModel(binding: ActivityWebViewBinding, viewModel: WebViewViewModel) {
        binding.viewModel = viewModel
        with(binding.webView) {
            addInterceptor(adBlockInterceptor)
            addInterceptor(pdfInterceptor)
//            loadUrl("https://www.imslp.org")
            loadUrl("https://www.orimi.com/pdf-test.pdf")
//            loadUrl("https://imslp.org/wiki/Special:IMSLPDisclaimerAccept/548820")
        }
    }

    override fun getViewModelClass() = WebViewViewModel::class.java

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        var deepLinkStartable = object : DeepLinkStartable {
            override fun start(context: Context, deepLink: DeepLink) {
                context.startActivity(Intent(context, WebViewActivity::class.java))
            }
        }
    }
}