package com.liuqiang.gankforandroid.model.net.retrofit

import com.liuqiang.gankforandroid.util.FileUtils
import com.liuqiang.gankforandroid.util.safeLet
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody

class HttpMockInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        var mockFileName: String? = null

        val path = chain.request().url().url().path
        if (path.matches("^/api/day/history$".toRegex())) {
            mockFileName = "result_history.json"
        } else if (path.matches("^/api/day/\\d+/\\d+/\\d+$".toRegex())) {
            mockFileName = "result_daily.json"
        }  else if (path.matches("^/api/data/%E7%A6%8F%E5%88%A9/\\d+/\\d+$".toRegex())) {
            mockFileName = "result_type_girl.json"
        }else if (path.matches("^/api/data/[\\w%]+/\\d+/\\d+$".toRegex())) {
            mockFileName = "result_type.json"
        }

        val responseString = safeLet(mockFileName) {
            FileUtils.getStringFromResource(this.javaClass, "/json/" + mockFileName, "UTF-8")
        }

        val responseBody = safeLet(responseString) {
            ResponseBody.create(null, responseString)
        }

        return Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(if (responseBody != null) 200 else 404)
                .body(responseBody)
                .build()
    }
}