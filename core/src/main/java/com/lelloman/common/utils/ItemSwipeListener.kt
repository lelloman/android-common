package com.lelloman.common.utils

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

@Suppress("unused")
object ItemSwipeListener {

    fun set(recyclerView: RecyclerView, onSwipeListener: (position: Int) -> Unit) {

        val itemTouchCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ) = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                    onSwipeListener.invoke(viewHolder.adapterPosition)
                }
            }

        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(recyclerView)
    }
}