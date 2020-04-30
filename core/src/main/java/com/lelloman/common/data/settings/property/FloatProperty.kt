package com.lelloman.common.data.settings.property

import android.content.SharedPreferences

fun SharedPreferences.floatProperty(key: String, default: Float) = FloatProperty(key, default, this)

class FloatProperty(key: String, default: Float, prefs: SharedPreferences) :
    PrefsProperty<Float>(key, default, prefs) {

    override fun getValueFromPrefs(prefs: SharedPreferences) = prefs.getFloat(key, default)

    override fun putValueInPrefs(editor: SharedPreferences.Editor, value: Float) {
        editor.putFloat(key, value)
    }
}