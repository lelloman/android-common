package com.lelloman.common.http.request

enum class HttpRequestMethod(
    val value: String
) {
    GET("GET"),
    HEAD("HEAD"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE")
}