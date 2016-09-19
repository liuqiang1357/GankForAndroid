package com.liuqiang.gankforandroid.di.component

import android.app.Activity
import android.app.Application
import android.support.v4.app.Fragment
import com.liuqiang.gankforandroid.di.module.FragmentModule
import com.liuqiang.gankforandroid.di.scope.PerFragment
import com.liuqiang.gankforandroid.view.fragment.*
import com.liuqiang.gankforandroid.view.fragment.base.SimpleBaseFragment
import dagger.Component

@PerFragment
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(FragmentModule::class))
interface FragmentComponent {

    fun getApplication(): Application

    fun getActivity(): Activity

    fun getFragment(): Fragment

    fun inject(simpleBaseFragment: SimpleBaseFragment)

    fun inject(dailyFragment: DailyFragment)

    fun inject(dailyDetailFragment: DailyDetailFragment)

    fun inject(typeFragment: TypeFragment)

    fun inject(typeGankFragment: TypeGankFragment)

    fun inject(typeGirlFragment: TypeGirlFragment)

    fun inject(collectionFragment: CollectionFragment)
}
