package com.lelloman.demoapp.navigation

import com.lelloman.common.navigation.DeepLinkStartable
import com.lelloman.common.navigation.NavigationScreen
import com.lelloman.demoapp.ui.themeswitch.ThemeSwitchActivity
import com.lelloman.demoapp.ui.webview.WebViewActivity

enum class DemoAppScreens(override val deepLinkStartable: DeepLinkStartable) : NavigationScreen {
    THEMES_SWITCH(ThemeSwitchActivity.deepLinkStartable),
    WEB_VIEW(WebViewActivity.deepLinkStartable)
}