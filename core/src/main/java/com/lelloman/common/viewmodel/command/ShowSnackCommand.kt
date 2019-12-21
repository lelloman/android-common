package com.lelloman.common.viewmodel.command

import com.google.android.material.snackbar.Snackbar

data class ShowSnackCommand(
    val message: String,
    val actionLabel: String? = null,
    val actionToken: String? = null,
    val duration: Int = Snackbar.LENGTH_LONG
) : Command {

    val hasAction = actionLabel != null && actionToken != null
}