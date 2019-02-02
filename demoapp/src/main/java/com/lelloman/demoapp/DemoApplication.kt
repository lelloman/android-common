package com.lelloman.demoapp

import android.app.Activity
import android.app.Application
import android.content.Context
import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.demoapp.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

open class DemoApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector() = dispatchingActivityAndroidInjector

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        inject()
    }

    protected open fun inject() {
        DaggerAppComponent
            .builder()
            .baseApplicationModule(BaseApplicationModule(this))
            .build()
            .inject(this)
    }
}