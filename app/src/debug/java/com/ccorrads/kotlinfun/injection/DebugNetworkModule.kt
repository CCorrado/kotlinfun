package com.ccorrads.kotlinfun.injection

import com.ccorrads.kotlinfun.MainApp
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class DebugNetworkModule : NetworkModule() {

    @Override
    fun providesOkClient(application: MainApp): OkHttpClient {
        val cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(application))
        val okHttpBuilder = OkHttpClient.Builder()
        okHttpBuilder.addNetworkInterceptor(StethoInterceptor())
        okHttpBuilder.addInterceptor(defaultInterceptor)
        okHttpBuilder.addInterceptor(authInterceptor)

        // init okhttp 3 logger
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        okHttpBuilder.addInterceptor(logging)

        //Handle cookies
        okHttpBuilder.cookieJar(cookieJar)

        return okHttpBuilder.build()
    }

}