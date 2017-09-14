package com.ccorrads.kotlinfun

import com.facebook.stetho.Stetho
import com.ccorrads.kotlinfun.injection.DaggerBaseComponent
import com.ccorrads.kotlinfun.injection.DebugNetworkModule
import net.danlew.android.joda.JodaTimeAndroid

class MainAppDebug : MainApp() {

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)

        application = this
        component = DaggerBaseComponent.builder()
                .application(this)
                .networkModule(networkModule = DebugNetworkModule())
                .build()

        Stetho.initializeWithDefaults(this)

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build())
    }
}