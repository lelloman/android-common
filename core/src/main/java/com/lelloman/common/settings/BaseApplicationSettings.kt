package com.lelloman.common.settings

import android.content.Context
import com.lelloman.common.settings.property.booleanProperty
import com.lelloman.common.settings.property.namedProperty
import com.lelloman.common.view.AppTheme
import com.lelloman.common.view.AppThemes
import io.reactivex.Observable

interface BaseApplicationSettings {

    val useMeteredNetwork: Observable<Boolean>

    val appTheme: Observable<AppTheme>

    fun readAllSettings()

    fun reset()

    fun setUseMeteredNetwork(useMeteredNetwork: Boolean)

    fun setAppTheme(appTheme: AppTheme)

    companion object {
        const val SHARED_PREFS_NAME = "BaseApplicationSettings"

        const val KEY_USE_METERED_NETWORK = "UseMeteredNetwork"
        const val DEFAULT_USE_METERED_NETWORK = false

        const val KEY_APP_THEME = "AppTheme"
    }
}

internal class BaseApplicationSettingsImpl(
    context: Context,
    defaultAppTheme: AppTheme
) : BaseApplicationSettings {

    private val prefs = context.getSharedPreferences(
        BaseApplicationSettings.SHARED_PREFS_NAME,
        Context.MODE_PRIVATE
    )

    private val useMeteredNetworkProperty = prefs
        .booleanProperty(
            BaseApplicationSettings.KEY_USE_METERED_NETWORK,
            BaseApplicationSettings.DEFAULT_USE_METERED_NETWORK
        )

    private val appThemeProperty = prefs
        .namedProperty(
            BaseApplicationSettings.KEY_APP_THEME,
            defaultAppTheme,
            AppThemes::get
        )

    override val useMeteredNetwork = useMeteredNetworkProperty.observable
    override val appTheme = appThemeProperty.observable

    init {
        readAllSettings()
    }

    override fun readAllSettings() {
        useMeteredNetworkProperty.readValue()
        appThemeProperty.readValue()
    }

    override fun reset() {
        prefs.edit().clear().apply()
        readAllSettings()
    }

    override fun setUseMeteredNetwork(useMeteredNetwork: Boolean) =
        useMeteredNetworkProperty.set(useMeteredNetwork)

    override fun setAppTheme(appTheme: AppTheme) = appThemeProperty.set(appTheme)

}