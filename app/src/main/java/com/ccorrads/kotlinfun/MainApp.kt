package com.ccorrads.kotlinfun

import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.ccorrads.kotlinfun.injection.BaseComponent
import com.ccorrads.kotlinfun.injection.NetworkModule
import com.ccorrads.kotlinfun.receivers.NetworkChangeReceiver
import com.ccorrads.kotlinfun.injection.DaggerBaseComponent
import net.danlew.android.joda.JodaTimeAndroid

open class MainApp : Application() {

    companion object {
        lateinit var application: com.ccorrads.kotlinfun.MainApp
        lateinit var component: BaseComponent
    }

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
        com.ccorrads.kotlinfun.MainApp.Companion.application = this
        com.ccorrads.kotlinfun.MainApp.Companion.component = DaggerBaseComponent.builder()
                .application(this)
                .networkModule(networkModule = NetworkModule())
                .build()
        registerReceiver(NetworkChangeReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }
}