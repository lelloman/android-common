@file:Suppress("unused")

package com.lelloman.common.utils

import android.content.Context
import android.view.LayoutInflater

val Context.layoutInflater: LayoutInflater get() = LayoutInflater.from(this)