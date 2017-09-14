package com.ccorrads.kotlinfun.events

/**
 * EventBus event
 * All server related events should extend this class
 *
 * We can add more status codes here as we need them (ex CALL_UNAUTHORIZED, CALL_FORBIDDEN, etc.)
 * and also http status code and/or server error message if we need them at any point.
 */
abstract class AbstractServerEvent {

    companion object {
        val CALL_SUCCESSFUL: Int = 1
        val CALL_FAILED: Int = 2
    }
}