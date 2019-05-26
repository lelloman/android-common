package com.lelloman.common.webview.interceptor.pdf

import android.net.Uri


interface PdfUriOpener {
    fun openPdfUri(uri: Uri)
}