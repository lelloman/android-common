package com.lelloman.demoapp

import android.arch.lifecycle.LiveData
import com.lelloman.common.viewmodel.BaseViewModel

abstract class MainViewModel(dependencies: BaseViewModel.Dependencies) : BaseViewModel(dependencies) {

    abstract val helloText: LiveData<String>
}