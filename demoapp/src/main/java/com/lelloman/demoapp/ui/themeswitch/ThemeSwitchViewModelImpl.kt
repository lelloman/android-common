package com.lelloman.demoapp.ui.themeswitch

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.lelloman.common.utils.LazyLiveData
import com.lelloman.common.view.AppTheme
import com.lelloman.common.viewmodel.BaseViewModel

class ThemeSwitchViewModelImpl(
    dependencies: BaseViewModel.Dependencies
) : ThemeSwitchViewModel(dependencies) {

    private val mutableThemes: MutableLiveData<List<ThemeListItem>> by LazyLiveData {
        subscription {
            settings
                .appTheme
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe { selectedTheme ->
                    mutableThemes.postValue(
                        AppTheme
                            .values()
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
    }

    override val themes: LiveData<List<ThemeListItem>> = mutableThemes

    override fun onThemeClicked(appTheme: AppTheme) {
        settings.setAppTheme(appTheme)
    }
}