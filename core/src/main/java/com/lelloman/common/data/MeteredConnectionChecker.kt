package com.lelloman.common.data

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.RequiresPermission

interface MeteredConnectionChecker {
    fun isNetworkMetered(): Boolean
}

internal class MeteredConnectionCheckerImpl(context: Context) :
    MeteredConnectionChecker {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override fun isNetworkMetered() = connectivityManager.isActiveNetworkMetered
}