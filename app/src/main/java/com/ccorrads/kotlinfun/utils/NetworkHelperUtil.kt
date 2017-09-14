package com.ccorrads.kotlinfun.utils

import android.content.Context
import android.net.ConnectivityManager
import com.ccorrads.kotlinfun.MainApp

/**
 * Generic reusable network methods.
 */
object NetworkHelperUtil {

    /**
     * @param context to use to check for network connectivity.
     * @return true if connected, false otherwise.
     */
    private fun isOnline(context: Context): Boolean {
        val connMgr: ConnectivityManager
                = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo?.isConnected == true
    }

    fun isOnline(): Boolean {
        return isOnline(MainApp.application)
    }
}
