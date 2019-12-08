package com.lelloman.demoapp.ui.themeswitch

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.DeepLinkStartable
import com.lelloman.common.view.BaseActivity
import com.lelloman.common.view.adapter.BaseRecyclerViewAdapter
import com.lelloman.demoapp.R
import com.lelloman.demoapp.databinding.ActivityThemeSwitchBinding
import com.lelloman.demoapp.databinding.ListItemThemeBinding
import org.koin.android.viewmodel.ext.android.viewModel

private typealias ThemeListItem = ThemeSwitchViewModel.ThemeListItem

class ThemeSwitchActivity : BaseActivity<ThemeSwitchViewModel, ActivityThemeSwitchBinding>() {

    override val layoutResId = R.layout.activity_theme_switch

    override val viewModel by viewModel<ThemeSwitchViewModel>()

    override fun setViewModel(
        binding: ActivityThemeSwitchBinding,
        viewModel: ThemeSwitchViewModel
    ) {
        binding.viewModel = viewModel
        binding.themesRecyclerView.layoutManager = GridLayoutManager(this, 2)
        val adapter = ThemesAdapter { viewModel.onThemeClicked(it.theme) }
        binding.themesRecyclerView.adapter = adapter
        viewModel.themes.observe(this, adapter)
    }

    companion object {
        var deepLinkStartable = object : DeepLinkStartable {
            override fun start(context: Context, deepLink: DeepLink) {
                val intent = Intent(context, ThemeSwitchActivity::class.java)
                if (context !is Activity) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }
        }
            internal set
    }

    class ThemesAdapter(
        private val onThemeClickedListener: (ThemeListItem) -> Unit
    ) : BaseRecyclerViewAdapter<Long, ThemeListItem, ThemeListItemViewModel, ListItemThemeBinding>(
        onItemClickListener = onThemeClickedListener
    ) {
        override val listItemLayoutResId = R.layout.list_item_theme

        override fun bindViewModel(
            binding: ListItemThemeBinding,
            viewModel: ThemeListItemViewModel
        ) {
            binding.viewModel = viewModel
        }

        override fun createViewModel(viewHolder: BaseViewHolder<Long, ThemeListItem, ThemeListItemViewModel, ListItemThemeBinding>) =
            ThemeListItemViewModel(onThemeClickedListener)
    }
}