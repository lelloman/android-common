package com.lelloman.common.http.internal

import com.lelloman.common.http.ContentType
import com.lelloman.common.http.HttpClient
import com.lelloman.common.http.HttpClientException
import com.lelloman.common.http.HttpResponse
import com.lelloman.common.http.request.HttpRequest
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.utils.TimeProvider
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class HttpClientImpl(
    loggerFactory: LoggerFactory,
    private val okHttpClient: OkHttpClient,
    private val timeProvider: TimeProvider
) : HttpClient {

    private val logger = loggerFactory.getLogger(javaClass)

    override fun request(request: HttpRequest): Single<HttpResponse> = Single.fromCallable {
        try {
            val okRequestBody = request.body?.let {
                RequestBody.create(null, it.content)
            }
            val okRequest = Request.Builder()
                .url(request.url)
                .method(request.method.name, okRequestBody)
                .apply {
                    request.headers.entries.forEach { entry ->
                        addHeader(entry.key ?: "", entry.value ?: "")
                    }
                    if (okRequestBody != null) {
                        addHeader("Content-Length", okRequestBody.contentLength().toString())
                    }
                }
                .build()

            logger.d("Thread ${Thread.currentThread().name}")
            val bodyString = request.body?.content?.let { String(it) }
            logger.d("--> ${okRequest.method()} ${okRequest.url()}\n${okRequest.headers()}\nbody: $bodyString")

            val t1 = timeProvider.nowUtcMs()
            val okResponse = okHttpClient.newCall(okRequest).execute()
            val t2 = timeProvider.nowUtcMs()
            val body = okResponse.body()?.bytes() ?: ByteArray(0)
            logger.d("<-- ${okRequest.method()} ${okResponse.code()} ${okRequest.url()} in ${t2 - t1}ms\n${okResponse.headers()}")
            logger.d("response length: ${body.size}")

            val contentTypeHeader = okResponse.header("content-type", null)
            val contentType = ContentType.fromHeader(contentTypeHeader)
            val headers = okResponse.headers().toMultimap()
            HttpResponse(
                code = okResponse.code(),
                isSuccessful = okResponse.isSuccessful,
                body = body,
                contentType = contentType,
                headers = headers
            )
        } catch (throwable: Throwable) {
            throw HttpClientException(throwable)
        }
    }
}