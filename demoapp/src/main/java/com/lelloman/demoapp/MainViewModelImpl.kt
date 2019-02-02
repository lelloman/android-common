package com.lelloman.demoapp

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.lelloman.common.viewmodel.BaseViewModel

class MainViewModelImpl(dependencies: BaseViewModel.Dependencies) : MainViewModel(dependencies) {

    private val mutableHelloText = MutableLiveData<String>().apply { postValue("Hello library") }

    override val helloText: LiveData<String> = mutableHelloText
}