package com.lelloman.common.view

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.core.content.res.ResourcesCompat

interface ResourceProvider {
    fun getString(@StringRes stringId: Int, vararg args: Any): String
    fun getQuantityString(@PluralsRes resId: Int, quantity: Int, vararg formatArgs: Any): String
    fun getStringArray(@ArrayRes arrayId: Int): Array<String>
    fun getDrawable(@DrawableRes drawableId: Int): Drawable
    fun getColor(@ColorRes colorId: Int): Int
}

class ResourceProviderImpl(private val context: Context) : ResourceProvider {

    private val resources by lazy { context.resources }

    override fun getString(@StringRes stringId: Int, vararg args: Any): String =
        context.getString(stringId, *args)

    override fun getStringArray(arrayId: Int): Array<String> =
        resources.getStringArray(arrayId)

    override fun getDrawable(drawableId: Int): Drawable =
        ResourcesCompat.getDrawable(resources, drawableId, null)!!

    override fun getQuantityString(resId: Int, quantity: Int, vararg formatArgs: Any) =
        resources.getQuantityString(resId, quantity, *formatArgs)

    override fun getColor(colorId: Int) = ResourcesCompat.getColor(resources, colorId, null)
}