package com.lelloman.demoapp.di

import com.lelloman.common.view.InjectableActivity
import com.lelloman.demoapp.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
interface AndroidContributes {

    @ContributesAndroidInjector
    fun contributeBaseActivity(): InjectableActivity

    @ContributesAndroidInjector
    fun contributesMainActivity(): MainActivity
}