package com.ccorrads.kotlinfun.injection;

import com.ccorrads.kotlinfun.MainApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BaseModule {

    @Provides
    @Singleton
    fun provideContext(application: MainApp): MainApp {
        return application
    }

}