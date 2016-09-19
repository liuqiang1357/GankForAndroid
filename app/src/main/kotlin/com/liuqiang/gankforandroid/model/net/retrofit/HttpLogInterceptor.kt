package com.liuqiang.gankforandroid.model.net.retrofit

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class HttpLogInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response? {
        val request = chain.request()
        println("HttpLogInterceptor: " + request.hashCode() + ", " + request)

        var response: Response? = null
        val t1 = System.nanoTime()

        try {
            response = chain.proceed(request)
        } finally {
            val t2 = System.nanoTime()
            val time = (t2 - t1) / 1e9
            println("HttpLogInterceptor: " + request.hashCode() + ", " + request
                    + ", " + response + ", " + Thread.currentThread() + ", " + time + "s")
        }
        return response
    }
}
