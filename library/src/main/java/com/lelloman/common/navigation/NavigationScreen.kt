package com.lelloman.common.navigation

import com.lelloman.common.utils.model.Named
import kotlin.reflect.KClass

interface NavigationScreen : Named {
    val deepLinkStartable: DeepLinkStartable
}
