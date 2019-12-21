package com.lelloman.common.viewmodel.command

class PackageIntentNavigationEvent(
    val packageName: String,
    val activityName: String?
) : Command