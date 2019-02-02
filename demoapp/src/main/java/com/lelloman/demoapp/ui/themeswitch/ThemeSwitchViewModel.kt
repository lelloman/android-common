package com.lelloman.demoapp.ui.themeswitch

import android.arch.lifecycle.LiveData
import com.lelloman.common.utils.model.ModelWithId
import com.lelloman.common.view.AppTheme
import com.lelloman.common.viewmodel.BaseViewModel

abstract class ThemeSwitchViewModel(dependencies: BaseViewModel.Dependencies) : BaseViewModel(dependencies) {

    abstract val themes: LiveData<List<ThemeListItem>>

    abstract fun onThemeClicked(appTheme: AppTheme)

    class ThemeListItem(
        override val id: Long,
        val theme: AppTheme,
        var isEnabled: Boolean
    ) : ModelWithId<Long>
}