package com.lelloman.common.http

@Suppress("ArrayInDataClass")
data class HttpResponse(
    val code: Int,
    val isSuccessful: Boolean,
    val headers: Map<String, List<String>> = emptyMap(),
    val body: ByteArray = byteArrayOf(),
    val contentType: ContentType = UnknownContentType()
) {
    val stringBody by lazy { String(body) }
}