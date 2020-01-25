package com.lelloman.demoapp.ui.themeswitch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lelloman.common.data.model.ModelWithId
import com.lelloman.common.utils.LazyLiveData
import com.lelloman.common.view.AppTheme
import com.lelloman.common.view.AppThemes
import com.lelloman.common.viewmodel.BaseViewModel

class ThemeSwitchViewModel(dependencies: Dependencies) : BaseViewModel(dependencies) {

    private val mutableThemes: MutableLiveData<List<ThemeListItem>> by LazyLiveData {
        settings
            .appTheme
            .viewModelSubscription { selectedTheme ->
                mutableThemes.postValue(
                    AppThemes
                        .themes
                        .toList()
                        .mapIndexed { index, appTheme ->
                            ThemeListItem(
                                id = index.toLong(),
                                theme = appTheme,
                                isEnabled = appTheme == selectedTheme
                            )
                        }
                )
            }
    }
    val themes: LiveData<List<ThemeListItem>> = mutableThemes

    fun onThemeClicked(appTheme: AppTheme) {
        settings.setAppTheme(appTheme)
    }

    class ThemeListItem(
        override val id: Long,
        val theme: AppTheme,
        var isEnabled: Boolean
    ) : ModelWithId<Long>
}