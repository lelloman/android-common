package com.lelloman.demoapp

import com.lelloman.common.view.BaseActivity
import com.lelloman.demoapp.databinding.ActivityMainBinding

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override val layoutResId = R.layout.activity_main

    override fun setViewModel(binding: ActivityMainBinding, viewModel: MainViewModel) {
        binding.viewModel = viewModel
    }

    override fun getViewModelClass() = MainViewModel::class.java

}
