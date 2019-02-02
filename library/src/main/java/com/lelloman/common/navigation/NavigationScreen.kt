package com.lelloman.common.navigation

import com.lelloman.common.utils.model.Named

interface NavigationScreen : Named {
    val deepLinkStartable: DeepLinkStartable
}
