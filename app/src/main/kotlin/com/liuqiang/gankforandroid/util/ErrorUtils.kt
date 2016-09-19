package com.liuqiang.gankforandroid.util

import android.content.Context
import com.alibaba.fastjson.JSONException
import com.liuqiang.gankforandroid.BuildConfig
import com.liuqiang.gankforandroid.R
import java.io.IOException

object ErrorUtils {

    val ERROR_UNKNOWN_STR = R.string.error_unknown
    val ERROR_CONNECT_STR = R.string.error_connect
    val ERROR_RESULT_STR = R.string.error_result

    val ERROR_UNKNOWN = Error(ERROR_UNKNOWN_STR)
    val ERROR_CONNECT = Error(ERROR_CONNECT_STR)
    val ERROR_RESULT = Error(ERROR_RESULT_STR)

    fun getLocalString(context: Context, throwable: Throwable): String {
        if (throwable is Error) {
            val message = throwable.message
            if (!BuildConfig.SHOW_ORIGIN_ERROR || message == null) {
                if (throwable.resId != 0) {
                    return context.getString(throwable.resId)
                } else {
                    return context.getString(ERROR_UNKNOWN_STR)
                }
            }
            return message
        }
        return getLocalString(context, parseDefaultError(throwable))
    }

    private fun parseDefaultError(throwable: Throwable): Error {
        return when (throwable) {
            is Error -> throwable
            is IOException -> Error(ERROR_CONNECT_STR, throwable)
            is JSONException -> Error(ERROR_RESULT_STR, throwable)
            else -> Error(ERROR_UNKNOWN_STR, throwable)
        }
    }

    class Error : RuntimeException {

        var resId: Int = 0

        constructor() {
        }

        constructor(resId: Int) {
            this.resId = resId
        }

        constructor(resId: Int, cause: Throwable) : super(cause) {
            this.resId = resId
        }

        constructor(message: String) : super(message) {
        }

        constructor(message: String, cause: Throwable) : super(message, cause) {
        }

        constructor(cause: Throwable) : super(cause) {
        }
    }
}
