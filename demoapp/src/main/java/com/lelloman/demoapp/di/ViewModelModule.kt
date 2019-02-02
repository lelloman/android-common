package com.lelloman.demoapp.di

import android.arch.lifecycle.ViewModel
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.demoapp.MainViewModel
import com.lelloman.demoapp.MainViewModelImpl
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import javax.inject.Singleton

@Module
class ViewModelModule {

    @Singleton
    @Provides
    fun provideMap(): Map<Class<out ViewModel>, Provider<out ViewModel>> = mutableMapOf()

    @Provides
    fun provideMainViewModel(
        dependencies: BaseViewModel.Dependencies
    ): MainViewModel = MainViewModelImpl(
        dependencies = dependencies
    )
}