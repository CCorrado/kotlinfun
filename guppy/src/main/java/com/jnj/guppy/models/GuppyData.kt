package com.jnj.guppy.models

/**
 *
 * Model Class to hold Logged data from {GuppyInterceptor}
 *
 * Modeling after:
 *
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
 **/
data class GuppyData(val requestType: String?, val host: String?, val requestContentType: String?,
                     val requestContentLength: String?, val requestHeaders: List<String>,
                     val requestBody: String?, val responseContentType: String?,
                     val responseContentLength: String?, val responseResult: String?,
                     val responseHeaders: List<String>, val responseBody: String?)