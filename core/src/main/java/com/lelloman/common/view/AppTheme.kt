package com.lelloman.common.view

import androidx.annotation.StyleRes
import com.lelloman.common.R
import com.lelloman.common.utils.model.Named

enum class AppTheme(
    @StyleRes val resId: Int
) : Named {
    LIGHT(R.style.CustomTheme_Light),
    DARCULA(R.style.CustomTheme_Darcula),
    MOCKITO(R.style.CustomTheme_Mockito),
    BLACK(R.style.CustomTheme_Black),
    FOREST(R.style.CustomTheme_Forest);

    companion object {

        val DEFAULT = LIGHT

        private val namesMap = values().associateBy(AppTheme::name)

        fun fromName(name: String): AppTheme =
            if (namesMap.containsKey(name)) {
                namesMap[name] ?: DEFAULT
            } else {
                DEFAULT
            }
    }
}