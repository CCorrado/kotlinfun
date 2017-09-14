package com.ccorrads.kotlinfun.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.ccorrads.kotlinfun.events.AbstractServerEvent
import com.ccorrads.kotlinfun.events.NetworkChangedEvent
import com.ccorrads.kotlinfun.services.SyncService
import com.ccorrads.kotlinfun.utils.NetworkHelperUtil
import org.greenrobot.eventbus.EventBus


class NetworkChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (NetworkHelperUtil.isOnline()) {
            val syncServiceIntent = Intent(context, SyncService::class.java)
            try {
                context.startService(syncServiceIntent)
            } catch (error: IllegalStateException) {
                //If the app is in the background, this exception can be safely logged.
                Log.e(TAG, error.message, error)
            }
        }
        EventBus.getDefault().postSticky(NetworkChangedEvent(AbstractServerEvent.CALL_SUCCESSFUL))
    }

    companion object {

        private val TAG = NetworkChangeReceiver::class.java.simpleName
    }
}