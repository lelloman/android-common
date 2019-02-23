package com.lelloman.common.view.actionevent

import com.google.android.material.snackbar.Snackbar

data class SnackEvent(
    val message: String,
    val actionLabel: String? = null,
    val actionToken: String? = null,
    val duration: Int = Snackbar.LENGTH_LONG
) : ViewActionEvent {

    val hasAction = actionLabel != null && actionToken != null
}