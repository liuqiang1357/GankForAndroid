package com.liuqiang.gankforandroid.di.module

import android.app.Service
import com.liuqiang.gankforandroid.di.scope.PerService
import dagger.Module
import dagger.Provides

@Module
class ServiceModule(private val service: Service) {

    @Provides
    @PerService
    fun ProvideService(): Service {
        return service
    }
}
