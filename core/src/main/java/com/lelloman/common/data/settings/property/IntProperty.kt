package com.lelloman.common.data.settings.property

import android.content.SharedPreferences

fun SharedPreferences.intProperty(key: String, default: Int) =
    IntProperty(key, default, this)

class IntProperty(
    key: String,
    default: Int,
    prefs: SharedPreferences
) : PrefsProperty<Int>(key, default, prefs) {

    override fun getValueFromPrefs(prefs: SharedPreferences) = prefs.getInt(key, default)

    override fun putValueInPrefs(editor: SharedPreferences.Editor, value: Int) {
        editor.putInt(key, value)
    }
}