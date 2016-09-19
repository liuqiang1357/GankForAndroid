package com.liuqiang.gankforandroid.model.net.retrofit

import android.graphics.BitmapFactory
import com.liuqiang.gankforandroid.BuildConfig
import com.liuqiang.gankforandroid.model.entity.Image
import com.liuqiang.gankforandroid.model.net.ImageWebService
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import rx.Observable
import java.util.concurrent.TimeUnit

object OkHttpHelper {

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
                .retryOnConnectionFailure(true)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build()
    }

    fun getImageWebService(): ImageWebService {
        return object : ImageWebService {
            override fun getImageSize(image: Image): Observable<Image> {
                return Observable.defer {
                    try {
                        getOkHttpClient()
                                .newCall(Request.Builder().url(image.url).build())
                                .execute()
                                .body()
                                .byteStream()
                                .use {
                                    val options = BitmapFactory.Options()
                                    options.inJustDecodeBounds = true
                                    BitmapFactory.decodeStream(it, null, options)
                                    image.width = options.outWidth
                                    image.height = options.outHeight
                                }
                    } catch (e: Exception) {
                    }
                    Observable.just(image)
                }
            }
        }
    }
}
