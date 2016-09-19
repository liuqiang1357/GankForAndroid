package com.liuqiang.gankforandroid.util

import android.content.Context

object TypeUtils {

    val TYPE_ANDROID = "Android"
    val TYPE_IOS = "iOS"
    val TYPE_FRONT_END = "前端"
    val TYPE_RESOURCES = "拓展资源"
    val TYPE_RECOMMEND = "瞎推荐"
    val TYPE_GIRL = "福利"
    val TYPE_VIDEO = "休息视频"

    val TYPES = arrayOf(TYPE_ANDROID, TYPE_IOS, TYPE_FRONT_END, TYPE_RESOURCES, TYPE_RECOMMEND, TYPE_GIRL, TYPE_VIDEO)

    fun getRequestString(type: String): String {
        return type
    }

    fun getLocalString(context: Context, type: String): String {
        return type
    }
}
