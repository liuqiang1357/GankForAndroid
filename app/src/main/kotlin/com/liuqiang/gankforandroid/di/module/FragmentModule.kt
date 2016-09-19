package com.liuqiang.gankforandroid.di.module

import android.app.Activity
import android.support.v4.app.Fragment
import com.liuqiang.gankforandroid.di.scope.PerFragment
import dagger.Module
import dagger.Provides

@Module
class FragmentModule(private val fragment: Fragment) {

    @Provides
    @PerFragment
    fun provideActivity(): Activity {
        return fragment.activity
    }

    @Provides
    @PerFragment
    fun provideFragment(): Fragment {
        return fragment
    }
}
