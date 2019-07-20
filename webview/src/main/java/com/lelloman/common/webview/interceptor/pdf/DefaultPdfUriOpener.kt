package com.lelloman.common.webview.interceptor.pdf

import android.content.Context
import android.content.Intent
import android.net.Uri

class DefaultPdfUriOpener(private val context: Context) : PdfUriOpener {

    override fun openPdfUri(uri: String) = Intent(Intent.ACTION_VIEW)
        .setDataAndType(Uri.parse(uri), "application/pdf")
        .let(context::startActivity)
}