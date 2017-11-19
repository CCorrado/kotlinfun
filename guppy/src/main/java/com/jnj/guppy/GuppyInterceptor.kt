package com.jnj.guppy

import android.content.Context
import com.jnj.guppy.models.GuppyData
import com.jnj.guppy.models.InterceptedData
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.internal.http.HttpHeaders
import okio.Buffer
import java.io.EOFException
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

/**
 * HTTP Custom Interceptor Borrowed lovingly from the OKHTTP3 HttpURLInterceptor class.
 * The "Logging" Concept has been adapted to work with a data model that will drive the list of
 * Requests being sent out by the instance of OkHTTP this interceptor will be added to.
 */
class GuppyInterceptor(private val context: Context, private val level: Level) : Interceptor {

    private val UTF_8 = Charset.forName("UTF-8")

    enum class Level {
        /** No logs.  */
        NONE,
        /**
         * Logs request and response lines.
         *
         *
         * Example:
         * <pre>`--> POST /greeting http/1.1 (3-byte body)
         *
         * <-- 200 OK (22ms, 6-byte body)
        `</pre> *
         */
        BASIC,
        /**
         * Logs request and response lines and their respective headers.
         *
         *
         * Example:
         * <pre>`--> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         * <-- END HTTP
        `</pre> *
         */
        HEADERS,
        /**
         * Logs request and response lines and their respective headers and bodies (if present).
         *
         *
         * Example:
         * <pre>`--> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         *
         * Hi?
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         *
         * Hello!
         * <-- END HTTP
        `</pre> *
         */
        BODY
    }

    interface Logger {
        fun logRequestType(message: String)
        fun logHost(message: String)
        fun logRequestContentType(message: String)
        fun logRequestContentLength(message: String)
        fun logRequestHeaders(message: String)
        fun logRequestBody(message: String)
        fun logResponseResult(message: String)
        fun logResponseContentType(message: String)
        fun logResponseContentLength(message: String)
        fun logResponseHeaders(message: String)
        fun logResponseBody(message: String)

        companion object {
            var interceptedData = InterceptedData()
            /** A [Logger] defaults output appropriate for the current platform.  */
            val DEFAULT: Logger = object : Logger {
                override fun logRequestType(message: String) {
                    interceptedData.requestType = message
                }

                override fun logHost(message: String) {
                    interceptedData.host = message
                }

                override fun logRequestContentType(message: String) {
                    interceptedData.requestContentType = message
                }

                override fun logRequestContentLength(message: String) {
                    interceptedData.requestContentLength = message
                }

                override fun logRequestHeaders(message: String) {
                    interceptedData.requestHeaders += message
                }

                override fun logRequestBody(message: String) {
                    interceptedData.requestBody = message
                }

                override fun logResponseResult(message: String) {
                    interceptedData.responseResult = message
                }

                override fun logResponseContentType(message: String) {
                    interceptedData.responseContentType = message
                }

                override fun logResponseContentLength(message: String) {
                    interceptedData.responseContentLength = message
                }

                override fun logResponseHeaders(message: String) {
                    interceptedData.responseHeaders += message
                }

                override fun logResponseBody(message: String) {
                    interceptedData.responseBody = message
                }
            }
        }
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (this.level == Level.NONE) {
            return chain.proceed(request)
        }
        val logger = Logger.DEFAULT

        val logBody = level == Level.BODY
        val logHeaders = logBody || level == Level.HEADERS

        val requestBody = request.body()
        val hasRequestBody = requestBody != null

        val connection = chain.connection()
        val protocol = if (connection != null) connection.protocol() else Protocol.HTTP_1_1
        logger.logRequestType(request.method() + " " + protocol)
        logger.logHost(request.url().toString())

        if (!logHeaders && hasRequestBody) {
            logger.logRequestContentLength(" (" + requestBody?.contentLength() + "-byte body)")
        }

        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody?.contentType() != null) {
                    logger.logRequestContentType("Content-Type: " + requestBody.contentType())
                }
                if (requestBody?.contentLength() != -1L) {
                    logger.logRequestContentLength("Content-Length: " + requestBody.contentLength())
                }
            }

            var headerIndex = 0
            val headers = request.headers()
            val headerSize = headers.size()

            while (headerIndex < headerSize) {
                val name = headers.name(headerIndex)
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equals(name, ignoreCase = true) && !"Content-Length".equals(name, ignoreCase = true)) {
                    logger.logRequestHeaders(name + ": " + headers.value(headerIndex))
                }
                headerIndex++
            }

            if (!logBody || !hasRequestBody) {
                logger.logRequestBody("--> END " + request.method())
            } else if (bodyEncoded(request.headers())) {
                logger.logRequestBody("--> END " + request.method() + " (encoded body omitted)")
            } else {
                val buffer = Buffer()
                requestBody?.writeTo(buffer)

                var charset: Charset = UTF_8
                val contentType = requestBody.contentType()
                if (contentType != null) {
                    charset = contentType.charset(UTF_8)
                }

                if (isPlaintext(buffer)) {
                    logger.logRequestBody(buffer.readString(charset))
                } else {
                    logger.logRequestBody("--> END " + request.method() + " (binary "
                            + requestBody.contentLength() + "-byte body omitted)")
                }
            }
        }

        val startNs = System.nanoTime()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            logger.logResponseBody("<-- HTTP FAILED: " + e)
            throw e
        }

        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        val responseBody = response.body()
        val contentLength = responseBody?.contentLength()
        val bodySize = if (contentLength != -1L) {
            contentLength.toString() + "-byte"
        } else {
            "unknown-length"
        }

        logger.logResponseResult(("<-- " + response.code() + ' ' + response.message() + ' '
                + response.request().url() + " (" + tookMs + "ms" + (
                if (!logHeaders) ", $bodySize body" else "") + ')'))

        if (logHeaders) {
            val headers = response.headers()
            var headerIndex = 0
            val totalHeaders = headers.size()
            while (headerIndex < totalHeaders) {
                logger.logResponseHeaders(headers.name(headerIndex) + ": " + headers.value(headerIndex))
                headerIndex++
            }

            if (!logBody || !HttpHeaders.hasBody(response)) {
                logger.logResponseBody("<-- END HTTP")
            } else if (bodyEncoded(response.headers())) {
                logger.logResponseBody("<-- END HTTP (encoded body omitted)")
            } else {
                val source = responseBody.source()
                source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
                val buffer = source.buffer()

                var charset: Charset = UTF_8
                val contentType = responseBody.contentType()
                if (contentType != null) {
                    charset = contentType.charset(UTF_8)
                }

                if (!isPlaintext(buffer)) {
                    logger.logResponseBody("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)")
                    return response
                }

                if (contentLength != 0L) {
                    logger.logResponseBody(buffer.clone().readString(charset))
                }

                logger.logResponseContentLength("<-- END HTTP (" + buffer.size() + "-byte body)")
            }
        }
        val dataToSave = GuppyData(Logger.interceptedData.requestType, Logger.interceptedData.host,
                Logger.interceptedData.requestContentType, Logger.interceptedData.requestContentLength,
                Logger.interceptedData.requestHeaders, Logger.interceptedData.requestBody,
                Logger.interceptedData.responseContentType, Logger.interceptedData.responseContentLength,
                Logger.interceptedData.responseResult, Logger.interceptedData.responseHeaders,
                Logger.interceptedData.responseBody)

        //Clear any data in the cache
        Logger.interceptedData = InterceptedData()

        val sharedPrefs = SharedPrefsData()
        sharedPrefs.updateGuppyData(context, dataToSave)
        return response
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    private fun isPlaintext(buffer: Buffer): Boolean {
        try {
            val prefix = Buffer()
            val byteCount = if (buffer.size() < 64) {
                buffer.size()
            } else {
                64
            }
            buffer.copyTo(prefix, 0, byteCount)
            for (i in 0..15) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            return true
        } catch (e: EOFException) {
            return false // Truncated UTF-8 sequence.
        }
    }

    private fun bodyEncoded(headers: Headers): Boolean {
        val contentEncoding = headers.get("Content-Encoding")
        return contentEncoding != null && !contentEncoding.equals("identity", ignoreCase = true)
    }
}