package com.lelloman.demoapp.ui.scrollbehavior

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lelloman.common.view.BaseActivity
import com.lelloman.demoapp.R
import com.lelloman.demoapp.databinding.ActivityScrollBehaviorBinding
import org.koin.android.viewmodel.ext.android.viewModel


class ScrollBehaviorActivity :
    BaseActivity<ScrollBehaviorViewModel, ActivityScrollBehaviorBinding>() {

    override val viewModel by viewModel<ScrollBehaviorViewModel>()

    override val layoutResId = R.layout.activity_scroll_behavior

    override val hasRootContainer = false

    private val items = Array(100) { it.plus(1).toString() }

    override fun setViewModel(
        binding: ActivityScrollBehaviorBinding,
        viewModel: ScrollBehaviorViewModel
    ) {
        binding.viewModel = viewModel
        binding.recyclerView.adapter = Adapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        hideFabOnScroll(binding.fab, binding.recyclerView)
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, ScrollBehaviorActivity::class.java))
        }
    }

    private inner class Adapter : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = layoutInflater
            .inflate(R.layout.list_item_scroll_behavior_item, parent, false)
            .let(::ViewHolder)

        override fun getItemCount() = items.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.textView.text = items[position]
        }
    }

    private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView.findViewById<TextView>(R.id.textView)
    }
}