package com.liuqiang.gankforandroid

import android.app.Application
import com.liuqiang.gankforandroid.di.component.ApplicationComponent
import com.liuqiang.gankforandroid.di.component.DaggerApplicationComponent
import com.liuqiang.gankforandroid.di.module.ApplicationModule
import com.liuqiang.gankforandroid.util.SettingUtils
import com.orhanobut.logger.LogLevel
import com.orhanobut.logger.Logger
import com.squareup.leakcanary.LeakCanary

class App : Application() {

    lateinit var applicationComponent: ApplicationComponent; private set

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this)).build()

        if (BuildConfig.USE_LOGGER) {
            Logger.init("Logger").logLevel(LogLevel.FULL).hideThreadInfo()
        } else {
            Logger.init("Logger").logLevel(LogLevel.NONE)
        }

        if (BuildConfig.USE_LEAKCANERY) {
            LeakCanary.install(this)
        }

        if (!SettingUtils.isInit(this)) {
            applicationComponent.getGankDataService().initTypes().toBlocking().single()
            SettingUtils.setInit(this, true)
        }
    }
}
