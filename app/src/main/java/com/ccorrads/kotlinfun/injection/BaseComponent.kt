package com.ccorrads.kotlinfun.injection

import com.ccorrads.kotlinfun.MainApp
import com.ccorrads.kotlinfun.database.MealDbHelper
import com.ccorrads.kotlinfun.network.BackendService
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Component(modules = arrayOf(AndroidInjectionModule::class, BaseModule::class, NetworkModule::class))
@Singleton
interface BaseComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: MainApp): Builder

        fun networkModule(networkModule: NetworkModule): Builder

        fun build(): BaseComponent
    }

    fun getBackendService(): BackendService

    fun getMealDbHelper(): MealDbHelper
}