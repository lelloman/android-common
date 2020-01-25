package com.lelloman.common.utils

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.WindowManager
import com.lelloman.common.data.model.Position

@Suppress("unused")
class NavigationBarDetector(private val context: Context) {

    private val windowManager by lazy {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    fun getNavigationBarSpecs(): NavigationBarSpecs {
        val appUsableSize = getAppUsableSize()
        val realScreenSize = getRealScreenSize()

        return when {
            appUsableSize.x < realScreenSize.x -> NavigationBarSpecs(
                width = realScreenSize.x - appUsableSize.x,
                height = appUsableSize.y,
                position = Position.LEFT
            )
            appUsableSize.y < realScreenSize.y -> NavigationBarSpecs(
                width = appUsableSize.x,
                height = realScreenSize.y - appUsableSize.y,
                position = Position.BOTTOM
            )
            else -> NavigationBarSpecs(
                0,
                0,
                Position.NONE
            )
        }
    }

    private fun getAppUsableSize() = Point().apply {
        windowManager.defaultDisplay.getSize(this)
    }

    private fun getRealScreenSize() = Point().apply {
        if (Build.VERSION.SDK_INT > 16) {
            windowManager.defaultDisplay.getRealSize(this)
        } else {
            windowManager.defaultDisplay.getSize(this)
        }
    }
}
