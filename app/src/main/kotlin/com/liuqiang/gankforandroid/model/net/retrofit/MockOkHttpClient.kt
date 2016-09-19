package com.liuqiang.gankforandroid.model.net.retrofit

import com.liuqiang.gankforandroid.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object MockOkHttpClient {

    fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .apply {
                    if (BuildConfig.USE_HTTP_LOGGING)
                        addInterceptor(HttpLoggingInterceptor()
                                .setLevel(HttpLoggingInterceptor.Level.BASIC))
                }
                .apply {
                    if (BuildConfig.USE_HTTP_LOG)
                        addInterceptor(HttpLogInterceptor())
                }
                .addInterceptor(HttpMockInterceptor())
                .build()
    }
}
