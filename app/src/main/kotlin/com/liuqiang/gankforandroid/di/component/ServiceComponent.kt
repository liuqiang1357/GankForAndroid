package com.liuqiang.gankforandroid.di.component

import android.app.Service
import com.liuqiang.gankforandroid.di.module.ServiceModule
import com.liuqiang.gankforandroid.di.scope.PerService
import dagger.Component

@PerService
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(ServiceModule::class))
interface ServiceComponent {

    fun getService(): Service
}
