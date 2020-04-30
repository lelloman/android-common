package com.lelloman.common.data.settings.property

import android.content.SharedPreferences

fun SharedPreferences.stringProperty(key: String, default: String) =
    StringProperty(key, default, this)

class StringProperty(
    key: String,
    default: String,
    prefs: SharedPreferences
) : PrefsProperty<String>(key, default, prefs) {

    override fun putValueInPrefs(editor: SharedPreferences.Editor, value: String) {
        editor.putString(key, value)
    }

    override fun getValueFromPrefs(prefs: SharedPreferences) = prefs.getString(key, default)!!
}