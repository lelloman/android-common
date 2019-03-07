package com.lelloman.common.http.request

data class HttpRequest(
    val url: String,
    val method: HttpRequestMethod = HttpRequestMethod.GET,
    val body: HttpRequestBody? = null
)