package com.lelloman.common.jvmtestutils

import android.graphics.drawable.Drawable
import com.lelloman.common.data.ResourceProvider
import com.lelloman.common.view.AppTheme

class MockResourceProvider(
    private var defaultStringArrayLength: Int = 10
) : ResourceProvider {

    private val arraysMap: MutableMap<Int, Array<String>> = hashMapOf()

    override fun getString(stringId: Int, vararg args: Any) =
        "$stringId${args.joinToString(separator = "") { ":$it" }}"

    override fun getQuantityString(resId: Int, quantity: Int, vararg formatArgs: Any): String =
        "$resId:$quantity${formatArgs.joinToString(separator = "") { ":$it" }}"

    override fun getStringArray(arrayId: Int) = if (arraysMap.containsKey(arrayId)) {
        arraysMap[arrayId]!!
    } else {
        Array(defaultStringArrayLength) { "$it" }
    }

    override fun getColor(colorId: Int) = colorId

    override fun getInteger(intId: Int) = intId

    fun registerStringArray(arrayId: Int, array: Array<String>) {
        arraysMap[arrayId] = array
    }

    override fun getDrawable(drawableId: Int): Drawable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resolveColorAttribute(attrId: Int, defaultValue: Int, appTheme: AppTheme?) = attrId
}