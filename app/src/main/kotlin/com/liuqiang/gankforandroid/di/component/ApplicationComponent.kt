package com.liuqiang.gankforandroid.di.component

import android.app.Application
import com.liuqiang.gankforandroid.di.module.ApplicationModule
import com.liuqiang.gankforandroid.di.qualifier.Local
import com.liuqiang.gankforandroid.di.qualifier.Remote
import com.liuqiang.gankforandroid.di.scope.PerApplication
import com.liuqiang.gankforandroid.model.db.GankDataService
import com.liuqiang.gankforandroid.model.net.GankWebService
import com.liuqiang.gankforandroid.model.net.ImageWebService
import dagger.Component

@PerApplication
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

    fun getApplication(): Application

    fun getGankDataService(): GankDataService

    fun getGankWebService(): GankWebService

    @Local
    fun getLocalGankWebService(): GankWebService

    @Remote
    fun getRemoteGankWebService(): GankWebService

    fun getImageWebService(): ImageWebService
}

