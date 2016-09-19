package com.liuqiang.gankforandroid.di.module

import android.app.Application
import com.liuqiang.gankforandroid.BuildConfig
import com.liuqiang.gankforandroid.di.qualifier.Local
import com.liuqiang.gankforandroid.di.qualifier.Remote
import com.liuqiang.gankforandroid.di.scope.PerApplication
import com.liuqiang.gankforandroid.model.db.GankDataService
import com.liuqiang.gankforandroid.model.db.liteorm.LiteOrmHelper
import com.liuqiang.gankforandroid.model.net.GankWebService
import com.liuqiang.gankforandroid.model.net.ImageWebService
import com.liuqiang.gankforandroid.model.net.retrofit.MockRetrofitHelper
import com.liuqiang.gankforandroid.model.net.retrofit.OkHttpHelper
import com.liuqiang.gankforandroid.model.net.retrofit.RetrofitHelper
import com.liuqiang.gankforandroid.util.SettingUtils
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    @PerApplication
    fun provideApplication(): Application {
        return application
    }

    @Provides
    @PerApplication
    fun provideGankDataService(): GankDataService {
        return LiteOrmHelper.getGankDataService(application)
    }

    @Provides
    @PerApplication
    fun provideGankWebService(): GankWebService {
        if (BuildConfig.USE_LOCAL_DATA) {
            if (SettingUtils.getDataSource(application) == SettingUtils.DATA_SOURCE_LOCAL){
                return provideLocalGankWebService()
            }
        }
        return provideRemoteGankWebService()
    }

    @Provides
    @PerApplication
    @Local
    fun provideLocalGankWebService(): GankWebService {
        return MockRetrofitHelper.getGankWebService()
    }

    @Provides
    @PerApplication
    @Remote
    fun provideRemoteGankWebService(): GankWebService {
        return RetrofitHelper.getGankWebService()
    }

    @Provides
    @PerApplication
    fun provideImageWebService(): ImageWebService {
        return OkHttpHelper.getImageWebService()
    }
}
