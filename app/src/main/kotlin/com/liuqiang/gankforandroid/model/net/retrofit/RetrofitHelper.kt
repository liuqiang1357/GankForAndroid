package com.liuqiang.gankforandroid.model.net.retrofit

import com.liuqiang.gankforandroid.model.net.GankWebService
import com.liuqiang.gankforandroid.util.GANK_SERVER_BASE_URL
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.fastjson.FastJsonConverterFactory

object RetrofitHelper {

    fun getGankWebService(): GankWebService {
        return Retrofit.Builder()
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(GANK_SERVER_BASE_URL)
                .client(OkHttpHelper.getOkHttpClient())
                .build()
                .create(GankWebService::class.java)
    }
}
