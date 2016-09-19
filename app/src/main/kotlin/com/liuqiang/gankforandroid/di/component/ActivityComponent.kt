package com.liuqiang.gankforandroid.di.component

import android.app.Activity
import android.app.Application
import com.liuqiang.gankforandroid.di.module.ActivityModule
import com.liuqiang.gankforandroid.di.scope.PerActivity
import com.liuqiang.gankforandroid.view.activity.WebActivity
import com.liuqiang.gankforandroid.view.activity.base.SimpleBaseActivity
import com.liuqiang.gankforandroid.view.activity.base.SimpleSwipeBackActivity
import dagger.Component

@PerActivity
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(ActivityModule::class))
interface ActivityComponent {

    fun getApplication(): Application

    fun getActivity(): Activity

    fun inject(simpleBaseActivity: SimpleBaseActivity)

    fun inject(simpleSwipeBackActivity: SimpleSwipeBackActivity)

    fun inject(webActivity: WebActivity)
}
