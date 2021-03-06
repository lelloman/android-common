package com.lelloman.common.data

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.pm.PackageInfoCompat

interface ApplicationInfoProvider {
    val versionCode: Long
    val versionName: String
    val packageName: String
}

internal class ApplicationInfoProviderImpl(
    context: Context,
    packageManager: PackageManager
) : ApplicationInfoProvider {

    override val packageName: String = context.packageName

    override val versionCode: Long
    override val versionName: String

    init {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        versionCode = PackageInfoCompat.getLongVersionCode(packageInfo)
        versionName = packageInfo.packageName
    }
}