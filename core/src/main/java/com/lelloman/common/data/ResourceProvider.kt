package com.lelloman.common.data

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.*
import androidx.core.content.res.ResourcesCompat
import com.lelloman.common.view.AppTheme

interface ResourceProvider {
    fun getString(@StringRes stringId: Int, vararg args: Any): String
    fun getQuantityString(@PluralsRes resId: Int, quantity: Int, vararg formatArgs: Any): String
    fun getStringArray(@ArrayRes arrayId: Int): Array<String>
    fun getDrawable(@DrawableRes drawableId: Int): Drawable
    fun getColor(@ColorRes colorId: Int): Int
    fun resolveColorAttribute(
        @AttrRes attrId: Int,
        defaultValue: Int,
        appTheme: AppTheme? = null
    ): Int
}

class ResourceProviderImpl(private val context: Context) :
    ResourceProvider {

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

    override fun resolveColorAttribute(attrId: Int, defaultValue: Int, appTheme: AppTheme?): Int {
        val typedValue = TypedValue()
        val originalTheme = context.theme
        if (appTheme != null) {
            originalTheme.applyStyle(appTheme.resId, true)
        }
        val resolved = originalTheme.resolveAttribute(attrId, typedValue, true)
        return if (resolved) {
            typedValue.data
        } else {
            defaultValue
        }
    }
}