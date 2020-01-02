@file:Suppress("unused")

package com.lelloman.common.utils

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.BindingAdapter

interface OnKeyboardActionDoneListener {
    fun onKeyboardActionDone()
}

object BindingAdapters {

    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated(message = "use \"visible\" or \"visibleGone\"")
    @JvmStatic
    @BindingAdapter("viewVisible")
    fun bindViewVisibility(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("visible")
    fun bindVisible(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

    @JvmStatic
    @BindingAdapter("visibleGone")
    fun bindVisibleGone(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("onKeyboardActionDoneListener")
    fun setOnKeyboardActionDoneListener(view: TextView, listener: OnKeyboardActionDoneListener?) {
        if (listener == null) {
            view.setOnEditorActionListener(null)
        } else {
            view.setOnEditorActionListener { _, actionId, _ ->
                (actionId == EditorInfo.IME_ACTION_DONE)
                    .also { if (it) listener.onKeyboardActionDone() }
            }
        }
    }
}