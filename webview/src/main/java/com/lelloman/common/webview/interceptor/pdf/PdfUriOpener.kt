package com.lelloman.common.webview.interceptor.pdf

import android.content.Context
import android.net.Uri


interface PdfUriOpener {
    fun openUri(context: Context, uri: Uri)
}