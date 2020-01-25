package com.lelloman.common.view

import androidx.annotation.StyleRes
import com.lelloman.common.data.model.Named

data class AppTheme(
    override val name: String,
    @StyleRes val resId: Int,
    val isLight: Boolean
) : Named

object AppThemes {

    private val mutableThemes = mutableSetOf<AppTheme>()
    val themes: Set<AppTheme> get() = mutableThemes

    fun addThemes(newThemes: Set<AppTheme>) {
        newThemes.forEach { newTheme ->
            if (mutableThemes.any { it.name == newTheme.name }) {
                throw IllegalArgumentException("Theme with name ${newTheme.name} already registered!")
            }
            mutableThemes.add(newTheme)
        }
    }

    operator fun get(name: String) = mutableThemes.firstOrNull { it.name == name } ?: mutableThemes.first()
}