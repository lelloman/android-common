package com.lelloman.common.view

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ArrayRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

interface ResourceProvider {
    fun getString(@StringRes stringId: Int, vararg args: Any): String
    fun getStringArray(@ArrayRes arrayId: Int): Array<String>
    fun getDrawable(@DrawableRes drawableId: Int): Drawable
}

class ResourceProviderImpl(private val context: Context) : ResourceProvider {

    private val resources by lazy { context.resources }

    override fun getString(@StringRes stringId: Int, vararg args: Any): String =
        context.getString(stringId, *args)

    override fun getStringArray(arrayId: Int): Array<String> =
        resources.getStringArray(arrayId)

    override fun getDrawable(drawableId: Int): Drawable =
        resources.getDrawable(drawableId, null)
}