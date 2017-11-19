package com.jnj.guppy.models

class InterceptedData {

    var requestType: String? = null
    var host: String? = null
    var requestContentType: String? = null
    var requestContentLength: String? = null
    var requestBody: String? = null
    var requestHeaders: List<String> = ArrayList()
    var responseResult: String? = null
    var responseContentType: String? = null
    var responseContentLength: String? = null
    var responseHeaders: List<String> = ArrayList()
    var responseBody: String? = null
}