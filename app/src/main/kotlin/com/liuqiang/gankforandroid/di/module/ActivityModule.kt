package com.liuqiang.gankforandroid.di.module

import android.app.Activity
import com.liuqiang.gankforandroid.di.scope.PerActivity
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: Activity) {

    @Provides
    @PerActivity
    fun provideActivity(): Activity {
        return activity
    }
}
