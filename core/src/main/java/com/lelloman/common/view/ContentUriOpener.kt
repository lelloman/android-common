package com.lelloman.common.view

import android.content.Context
import android.net.Uri
import java.io.InputStream

interface ContentUriOpener {

    fun open(uri: Uri): InputStream?
}

internal class ContentUriOpenerImpl(
    context: Context
) : ContentUriOpener {

    private val contentResolver = context.contentResolver

    override fun open(uri: Uri): InputStream? = contentResolver.openInputStream(uri)
}