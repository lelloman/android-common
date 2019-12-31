package com.lelloman.common.settings.property

import android.content.SharedPreferences
import com.lelloman.common.utils.model.Named

fun <T : Named> SharedPreferences.namedProperty(
    key: String,
    default: T,
    nameToObject: (String) -> T
) = NamedProperty(
    key = key,
    default = default,
    nameToObject = nameToObject,
    prefs = this
)

class NamedProperty<T : Named>(
    key: String,
    default: T,
    prefs: SharedPreferences,
    private val nameToObject: (String) -> T
) : PrefsProperty<T>(key, default, prefs) {

    override fun getValueFromPrefs(prefs: SharedPreferences): T =
        nameToObject(prefs.getString(key, default.name)!!)

    override fun putValueInPrefs(editor: SharedPreferences.Editor, value: T) {
        editor.putString(key, value.name)
    }
}