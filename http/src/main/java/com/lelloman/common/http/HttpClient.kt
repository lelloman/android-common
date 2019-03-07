package com.lelloman.common.http

import com.lelloman.common.http.request.HttpRequest
import io.reactivex.Single

interface HttpClient {

    fun request(request: HttpRequest): Single<HttpResponse>
}