package com.lelloman.common.http.request

data class HttpRequest(
    val url: String,
    val method: HttpRequestMethod = HttpRequestMethod.GET,
    val headers: Map<String?, String?> = emptyMap(),
    val body: HttpRequestBody? = null,
    val host: String? = null
)