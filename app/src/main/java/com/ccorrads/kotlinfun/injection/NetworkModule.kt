package com.ccorrads.kotlinfun.injection

import com.ccorrads.kotlinfun.BuildConfig
import com.ccorrads.kotlinfun.MainApp
import com.ccorrads.kotlinfun.R
import com.ccorrads.kotlinfun.database.DatabaseHelper
import com.ccorrads.kotlinfun.database.MealDbHelper
import com.ccorrads.kotlinfun.network.BackendService
import com.ccorrads.kotlinfun.network.CustomInterceptor
import com.ccorrads.kotlinfun.serialization.DateTimeTypeAdapter
import com.ccorrads.kotlinfun.serialization.LocalDateTypeAdapter
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jnj.guppy.GuppyInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.joda.time.DateTime
import org.joda.time.LocalDate
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton

/**
 * Dagger Singleton Module to define network based dependencies for all possible injected objects.
 * Mainly used for complex (multi-requirement) built objects, i.e. a Retrofit API Call.
 */
@Module
open class NetworkModule {

    companion object {

        private val formattedLocale: String
            get() =
                Locale.getDefault().language.toLowerCase() + "_" + Locale.getDefault().country.toUpperCase()

        // Customize the request
        val defaultInterceptor: Interceptor
            get() = Interceptor { chain ->
                val request = chain.request().newBuilder()
                        .header("version", BuildConfig.VERSION_NAME + "+" + BuildConfig.VERSION_CODE + "-android")
                        .header("id", BuildConfig.APPLICATION_ID)
                        .header("locale", formattedLocale)
                        .header("datetime", DateTime.now().toDateTimeISO().toString())
                        .build()
                chain.proceed(request)
            }

        val authInterceptor: Interceptor
            get() = CustomInterceptor()
    }

    @Singleton
    @Provides
    fun providesDbHelper(): DatabaseHelper =
            DatabaseHelper(MainApp.application, MainApp.application.getString(R.string.databaseName))

    @Singleton
    @Provides
    fun providesMealDbHelper(databaseHelper: DatabaseHelper): MealDbHelper =
            MealDbHelper(databaseHelper)

    @Singleton
    @Provides
    fun providesGson(): Gson {
        return GsonBuilder()
                .registerTypeAdapter(DateTime::class.java, DateTimeTypeAdapter().nullSafe())
                .registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter().nullSafe())
                .create()
    }

    @Singleton
    @Provides
    fun providesOkClient(): OkHttpClient {

        val cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(MainApp.application))
        val okHttpBuilder = OkHttpClient.Builder()
        okHttpBuilder.addInterceptor(defaultInterceptor)
        okHttpBuilder.addInterceptor(authInterceptor)
        okHttpBuilder.addInterceptor(GuppyInterceptor(MainApp.application, GuppyInterceptor.Level.BODY))
        //Handle cookies
        okHttpBuilder.cookieJar(cookieJar)

        return okHttpBuilder.build()
    }

    @Singleton
    @Provides
    fun providesBackendService(okHttpClient: OkHttpClient, gson: Gson): BackendService {
        return Retrofit.Builder()
                .baseUrl(MainApp.application.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
                .create(BackendService::class.java)
    }
}