package com.lelloman.demoapp.ui.main

import com.lelloman.common.view.BaseActivity
import com.lelloman.demoapp.R
import com.lelloman.demoapp.databinding.ActivityMainBinding
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override val viewModel by viewModel<MainViewModel>()

    override val layoutResId = R.layout.activity_main

    override fun setViewModel(binding: ActivityMainBinding, viewModel: MainViewModel) {
        binding.viewModel = viewModel
    }
}
