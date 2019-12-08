package com.lelloman.demoapp.ui.webview

import android.content.Context
import android.content.Intent
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.DeepLinkStartable
import com.lelloman.common.view.BaseActivity
import com.lelloman.demoapp.R
import com.lelloman.demoapp.databinding.ActivityWebViewBinding
import org.koin.android.viewmodel.ext.android.viewModel

class WebViewActivity : BaseActivity<WebViewViewModel, ActivityWebViewBinding>() {

    override val layoutResId = R.layout.activity_web_view

    override val viewModel by viewModel<WebViewViewModel>()

    override fun setViewModel(binding: ActivityWebViewBinding, viewModel: WebViewViewModel) {
        binding.viewModel = viewModel
    }

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