package com.lelloman.demoapp.ui.themeswitch

import com.lelloman.common.viewmodel.BaseListItemViewModel

class ThemeListItemViewModel(
    private val clickListener: (ThemeSwitchViewModel.ThemeListItem) -> Unit
) : BaseListItemViewModel<Long, ThemeSwitchViewModel.ThemeListItem> {

    var name: String = ""
        private set

    var isSelected = false
        private set

    private lateinit var item: ThemeSwitchViewModel.ThemeListItem

    fun onCheckedChanged(isChecked: Boolean) {
        clickListener(item)
    }

    override fun bind(item: ThemeSwitchViewModel.ThemeListItem) {
        this.item = item
        name = item.theme.name
        isSelected = item.isEnabled
    }
}