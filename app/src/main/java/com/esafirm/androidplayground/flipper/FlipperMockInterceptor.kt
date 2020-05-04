package com.esafirm.androidplayground.flipper

import android.text.TextUtils
import com.facebook.flipper.core.FlipperConnection
import com.facebook.flipper.core.FlipperObject
import com.facebook.flipper.plugins.common.BufferingFlipperPlugin.MockResponseConnectionListener
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkReporter
import com.facebook.flipper.plugins.network.NetworkReporter.ResponseInfo
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import java.io.IOException
import java.net.HttpURLConnection
import java.util.*

/**
 * An interceptor that connect to Flipper to handle request mocking
 */
class FlipperMockInterceptor @JvmOverloads constructor(
    private val plugin: NetworkFlipperPlugin,
    private val maxBodyBytes: Long = DEFAULT_MAX_BODY_BYTES,
    private val storage: MockStorage = MemoryMockStorage()
) : Interceptor, MockResponseConnectionListener {

    init {
        plugin.setConnectionListener(this)
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Check if there is a mock response
        val mockResponse = getMockResponse(request)
        val response = mockResponse ?: chain.proceed(request)

        if (mockResponse != null) {
            reportMock(request, response)
        }

        return response
    }

    private fun reportMock(request: Request, response: Response) {
        val identifier = UUID.randomUUID().toString()
        val body = response.body

        val jsonRequest = request.newBuilder()
            .addHeader("Content-Type", "application/json")
            .build()

        val requestInfo = convertRequestWithoutBody(jsonRequest, identifier)
        val responseInfo = convertResponse(response, body, identifier).apply {
            this.isMock = true
        }

        // Add request body
        // We can safely ignore this as some requests don't allow their body to be read more than once
        try {
            if (request.body != null) {
                requestInfo.body = bodyToByteArray(request, maxBodyBytes)
            }
        } catch (e: IOException) {
        }

        plugin.reportRequest(requestInfo)
        plugin.reportResponse(responseInfo)
    }

    @Throws(IOException::class)
    private fun convertRequestWithoutBody(request: Request, identifier: String): NetworkReporter.RequestInfo {
        val headers = convertHeader(request.headers)
        val info = NetworkReporter.RequestInfo()
        info.requestId = identifier
        info.timeStamp = System.currentTimeMillis()
        info.headers = headers
        info.method = request.method
        info.uri = request.url.toString()
        return info
    }

    @Throws(IOException::class)
    private fun convertResponse(response: Response, body: ResponseBody?, identifier: String): ResponseInfo {
        val headers = convertHeader(response.headers)
        val info = ResponseInfo()
        info.requestId = identifier
        info.timeStamp = response.receivedResponseAtMillis
        info.statusCode = response.code
        info.headers = headers
        var buffer: Buffer? = null
        try {
            val source = body!!.source()
            source.request(maxBodyBytes)
            buffer = source.buffer().clone()
            info.body = buffer.readByteArray()
        } finally {
            buffer?.close()
        }
        return info
    }

    private fun registerMockResponse(partialRequest: PartialRequestInfo, response: ResponseInfo) {
        storage.put(partialRequest, response)
    }

    private fun getMockResponse(request: Request): Response? {
        val url = request.url.toString()
        val method = request.method
        val partialRequest = PartialRequestInfo(url, method)
        if (!storage.contains(partialRequest)) {
            return null
        }
        val mockResponse = storage.get(partialRequest) ?: return null
        val builder = Response.Builder()
        builder
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(mockResponse.statusCode)
            .message(mockResponse.statusReason)
            .receivedResponseAtMillis(System.currentTimeMillis())
            .body(ResponseBody.create("application/text".toMediaTypeOrNull(), mockResponse.body));
        if (mockResponse.headers != null && mockResponse.headers.isNotEmpty()) {
            for (header in mockResponse.headers) {
                if (!TextUtils.isEmpty(header.name) && !TextUtils.isEmpty(header.value)) {
                    builder.header(header.name, header.value)
                }
            }
        }
        return builder.build()
    }

    private fun convertFlipperObjectRouteToResponseInfo(route: FlipperObject): ResponseInfo? {
        val data = route.getString("data")
        val requestUrl = route.getString("requestUrl")
        val method = route.getString("method")
        val headersArray = route.getArray("headers")
        if (TextUtils.isEmpty(requestUrl) || TextUtils.isEmpty(method)) {
            return null
        }
        val mockResponse = ResponseInfo()
        mockResponse.body = data.toByteArray()
        mockResponse.statusCode = HttpURLConnection.HTTP_OK
        mockResponse.statusReason = "OK"
        if (headersArray != null) {
            val headers: MutableList<NetworkReporter.Header> = ArrayList()
            for (j in 0 until headersArray.length()) {
                val header = headersArray.getObject(j)
                headers.add(NetworkReporter.Header(header.getString("key"), header.getString("value")))
            }
            mockResponse.headers = headers
        }
        return mockResponse
    }

    override fun onConnect(connection: FlipperConnection) {
        connection.receive(
            "mockResponses"
        ) { params, responder ->
            val array = params.getArray("routes")
            storage.clear()
            for (i in 0 until array.length()) {
                val route = array.getObject(i)
                val requestUrl = route.getString("requestUrl")
                val method = route.getString("method")
                val mockResponse = convertFlipperObjectRouteToResponseInfo(route)
                if (mockResponse != null) {
                    registerMockResponse(PartialRequestInfo(requestUrl, method), mockResponse)
                }
            }
            responder.success()
        }
    }

    override fun onDisconnect() {
    }

    companion object {
        // By default, limit body size (request or response) reporting to 100KB to avoid OOM
        private const val DEFAULT_MAX_BODY_BYTES = 100 * 1024.toLong()

        @Throws(IOException::class)
        private fun bodyToByteArray(request: Request, maxBodyBytes: Long): ByteArray? {
            val buffer = Buffer()
            if (request.body != null) {
                request.body!!.writeTo(buffer)
            }
            return buffer.readByteArray(buffer.size.coerceAtMost(maxBodyBytes))
        }

        private fun convertHeader(headers: Headers): List<NetworkReporter.Header> {
            val list: MutableList<NetworkReporter.Header> = ArrayList(headers.size)
            val keys = headers.names()
            for (key in keys) {
                list.add(NetworkReporter.Header(key, headers[key]))
            }
            return list
        }
    }
}