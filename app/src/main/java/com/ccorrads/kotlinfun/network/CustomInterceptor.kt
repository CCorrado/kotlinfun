package com.ccorrads.kotlinfun.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.HttpURLConnection

class CustomInterceptor : Interceptor {

    companion object {
        private val APP_UPDATE_REQUIRED = 422
    }

    private var isError: Boolean = false

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        // Customize the request
        val request = chain.request().newBuilder().build()

        val response = chain.proceed(request)

        val unAuthorized = response.code() == HttpURLConnection.HTTP_UNAUTHORIZED
        val forbidden = response.code() == HttpURLConnection.HTTP_FORBIDDEN
        val updateRequired = response.code() == APP_UPDATE_REQUIRED

        if (!isError) {
            if (unAuthorized) {
                isError = true
            }

            if (forbidden) {
                isError = true
                handleForbidden()
            }

            if (updateRequired) {
                isError = true
                handleUpdateRequired()
            }
        }

        return response
    }

    /**
     * Unauthorized (401) handling. Usually, attempt to refresh the session.
     */
    private fun handleUnAuthorized() {
        //TODO handle 401
        isError = false
    }

    /**
     * Forbidden (403) handling. Usually, bring the user out of the application.
     */
    private fun handleForbidden() {
        //TODO handle 403
        isError = false
    }

    /**
     * Update Required (422) handling. Usually, trigger similar event to forbidden (403)
     */
    private fun handleUpdateRequired() {
        //TODO handle 422
        isError = false
    }
}