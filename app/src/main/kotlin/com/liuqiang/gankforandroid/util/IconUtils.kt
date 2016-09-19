package com.liuqiang.gankforandroid.util

import com.liuqiang.gankforandroid.R

object IconUtils {
    fun getIconResId(url: String, type: String): Int {
        return if (TypeUtils.TYPE_VIDEO == type) {
            R.drawable.ic_url_video
        } else if (url.startsWith("http://blog.csdn.net")) {
            R.drawable.ic_url_csdn
        } else if (url.startsWith("https://github.com")) {
            R.drawable.ic_url_github
        } else if (url.startsWith("http://finalshares.com")) {
            R.drawable.ic_url_finalshares
        } else if (url.startsWith("http://www.jianshu.com")) {
            R.drawable.ic_url_jianshu
        } else if (url.startsWith("https://www.zhihu.com")) {
            R.drawable.ic_url_zhihu
        } else {
            R.drawable.ic_url_web
        }
    }
}
