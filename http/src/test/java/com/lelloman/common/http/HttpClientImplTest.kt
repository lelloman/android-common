package com.lelloman.common.http

import com.lelloman.common.http.internal.HttpClientImpl
import com.lelloman.common.http.request.HttpRequest
import com.lelloman.common.jvmtestutils.MockLoggerFactory
import com.lelloman.common.jvmtestutils.MockTimeProvider
import com.nhaarman.mockitokotlin2.*
import okhttp3.*
import org.junit.Test

class HttpClientImplTest {

    private val okResponseHeaders: Headers = mock {
        on { toMultimap() }.thenAnswer { emptyMap<String, List<String>>() }
    }
    private val okHttpResponse: Response = mock {
        on { headers() }.thenAnswer { okResponseHeaders }
    }
    private val okHttpCall: Call = mock {
        on { execute() }.thenAnswer { okHttpResponse }
    }

    private val okHttpClient: OkHttpClient = mock {
        on { newCall(any()) }.thenAnswer { okHttpCall }
    }
    private val loggerFactory = MockLoggerFactory()
    private val timeProvider = MockTimeProvider()

    private val tested = HttpClientImpl(
        okHttpClient = okHttpClient,
        loggerFactory = loggerFactory,
        timeProvider = timeProvider
    )

    @Test
    fun `executes ok http GET call`() {
        val tester = tested.request(HTTP_REQUEST).test()

        tester.assertNoErrors()
        verify(okHttpClient).newCall(argThat {
            this.method() == "GET" && this.url() == HttpUrl.parse(HTTP_REQUEST.url)
        })
    }

    @Test
    fun `returns http status code in response`() {
        val responseCode = 0xb00b5
        whenever(okHttpResponse.code()).thenReturn(responseCode)

        val tester = tested.request(HTTP_REQUEST).test()

        tester.assertNoErrors()
        tester.assertValue { it.code == responseCode }
    }

    @Test
    fun `returns successful property in response`() {
        whenever(okHttpResponse.isSuccessful).thenReturn(true)

        val tester = tested.request(HTTP_REQUEST).test()

        tester.assertNoErrors()
        tester.assertValue { it.isSuccessful }
    }

    @Test
    fun `returns unsuccessful property in response`() {
        whenever(okHttpResponse.isSuccessful).thenReturn(false)

        val tester = tested.request(HTTP_REQUEST).test()

        tester.assertNoErrors()
        tester.assertValue { !it.isSuccessful }
    }

    @Test
    fun `returns empty string body if ok http response body is null`() {
        whenever(okHttpResponse.body()).thenReturn(null)

        val tester = tested.request(HTTP_REQUEST).test()

        tester.assertNoErrors()
        tester.assertValue { it.stringBody == "" }
    }

    @Test
    fun `returns empty string body if string from ok http response body is null`() {
        val responseBody = ResponseBody.create(null, ByteArray(0))
        whenever(okHttpResponse.body()).thenReturn(responseBody)

        val tester = tested.request(HTTP_REQUEST).test()

        tester.assertNoErrors()
        tester.assertValue { it.stringBody == "" }
    }

    @Test
    fun `returns string body from ok http response`() {
        val body = "bo bo body"
        val responseBody = ResponseBody.create(null, body.toByteArray())
        whenever(okHttpResponse.body()).thenReturn(responseBody)

        val tester = tested.request(HTTP_REQUEST).test()

        tester.assertNoErrors()
        tester.assertValue { it.stringBody == body }
    }

    @Test
    fun `throws HttpClientException if ok http call throws exception`() {
        val exception = IllegalAccessException("smurfs")
        whenever(okHttpClient.newCall(any())).thenAnswer { throw exception }

        val tester = tested.request(HTTP_REQUEST).test()

        tester.assertError { it is HttpClientException && it.cause == exception }
    }

    @Test
    fun `throws HttpClientException if ok http call execution throws exception`() {
        val exception = IllegalAccessException("smurfs")
        whenever(okHttpCall.execute()).thenAnswer { throw exception }

        val tester = tested.request(HTTP_REQUEST).test()

        tester.assertError { it is HttpClientException && it.cause == exception }
    }

    @Test
    fun `returns application content type`() {
        val subType = "json"
        whenever(okHttpResponse.body()).thenReturn(ResponseBody.create(null, byteArrayOf(1, 2, 3)))
        whenever(okHttpResponse.header("content-type", null)).thenReturn("application/$subType")

        val tester = tested.request(HTTP_REQUEST).test()

        tester.assertNoErrors()
        tester.assertValue { it.contentType is ApplicationContentType && it.contentType.subType == subType }
    }

    @Test
    fun `returns audio content type`() {
        val subType = "mp3"
        whenever(okHttpResponse.body()).thenReturn(ResponseBody.create(null, byteArrayOf(1, 2, 3)))
        whenever(okHttpResponse.header("content-type", null)).thenReturn("audio/$subType")

        val tester = tested.request(HTTP_REQUEST).test()

        tester.assertNoErrors()
        tester.assertValue { it.contentType is AudioContentType && it.contentType.subType == subType }
    }

    @Test
    fun `returns image content type`() {
        val subType = "x-icon"
        whenever(okHttpResponse.body()).thenReturn(ResponseBody.create(null, byteArrayOf(1, 2, 3)))
        whenever(okHttpResponse.header("content-type", null)).thenReturn("image/$subType")

        val tester = tested.request(HTTP_REQUEST).test()

        tester.assertNoErrors()
        tester.assertValue { it.contentType is ImageContentType && it.contentType.subType == subType }
    }

    @Test
    fun `returns message content type`() {
        val subType = "what"
        whenever(okHttpResponse.body()).thenReturn(ResponseBody.create(null, byteArrayOf(1, 2, 3)))
        whenever(okHttpResponse.header("content-type", null)).thenReturn("message/$subType")

        val tester = tested.request(HTTP_REQUEST).test()

        tester.assertNoErrors()
        tester.assertValue { it.contentType is MessageContentType && it.contentType.subType == subType }
    }

    @Test
    fun `returns model content type`() {
        val subType = "what"
        whenever(okHttpResponse.body()).thenReturn(ResponseBody.create(null, byteArrayOf(1, 2, 3)))
        whenever(okHttpResponse.header("content-type", null)).thenReturn("model/$subType")

        val tester = tested.request(HTTP_REQUEST).test()

        tester.assertNoErrors()
        tester.assertValue { it.contentType is ModelContentType && it.contentType.subType == subType }
    }

    @Test
    fun `returns multipart content type`() {
        val subType = "what"
        whenever(okHttpResponse.body()).thenReturn(ResponseBody.create(null, byteArrayOf(1, 2, 3)))
        whenever(okHttpResponse.header("content-type", null)).thenReturn("multipart/$subType")

        val tester = tested.request(HTTP_REQUEST).test()

        tester.assertNoErrors()
        tester.assertValue { it.contentType is MultipartContentType && it.contentType.subType == subType }
    }

    @Test
    fun `returns text content type`() {
        val subType = "what"
        whenever(okHttpResponse.body()).thenReturn(ResponseBody.create(null, byteArrayOf(1, 2, 3)))
        whenever(okHttpResponse.header("content-type", null)).thenReturn("text/$subType")

        val tester = tested.request(HTTP_REQUEST).test()

        tester.assertNoErrors()
        tester.assertValue { it.contentType is TextContentType && it.contentType.subType == subType }
    }

    @Test
    fun `returns video content type`() {
        val subType = "what"
        whenever(okHttpResponse.body()).thenReturn(ResponseBody.create(null, byteArrayOf(1, 2, 3)))
        whenever(okHttpResponse.header("content-type", null)).thenReturn("video/$subType")

        val tester = tested.request(HTTP_REQUEST).test()

        tester.assertNoErrors()
        tester.assertValue { it.contentType is VideoContentType && it.contentType.subType == subType }
    }

    private companion object {
        val HTTP_REQUEST = HttpRequest("http://www.staceppa.com")
    }
}