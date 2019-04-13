package com.lelloman.demoapp.ui.webview

import android.content.Context
import android.content.Intent
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.DeepLinkStartable
import com.lelloman.common.view.BaseActivity
import com.lelloman.common.webview.CookedWebViewInjector
import com.lelloman.demoapp.R
import com.lelloman.demoapp.databinding.ActivityWebViewBinding
import javax.inject.Inject

class WebViewActivity : BaseActivity<WebViewViewModel, ActivityWebViewBinding>() {

    override val layoutResId = R.layout.activity_web_view

    @Inject
    lateinit var cookedWebViewInjector: CookedWebViewInjector

    override fun setViewModel(binding: ActivityWebViewBinding, viewModel: WebViewViewModel) {
        binding.viewModel = viewModel
        with(binding.webView) {
            cookedWebViewInjector.inject(this)
            progressBar = binding.progressBar
            loadUrl("https://www.repubblica.it")
        }
    }

    override fun getViewModelClass() = WebViewViewModel::class.java

    companion object {
        var deepLinkStartable = object : DeepLinkStartable {
            override fun start(context: Context, deepLink: DeepLink) {
                context.startActivity(Intent(context, WebViewActivity::class.java))
            }
        }
    }
}