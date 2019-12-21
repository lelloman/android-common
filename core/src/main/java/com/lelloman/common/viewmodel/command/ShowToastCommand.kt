package com.lelloman.common.viewmodel.command

import android.widget.Toast

data class ShowToastCommand(
    val message: String,
    val duration: Int = Toast.LENGTH_SHORT
) : Command