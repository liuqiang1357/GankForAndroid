package com.liuqiang.gankforandroid.util

import android.content.Context
import android.content.SharedPreferences

object SettingUtils {

    private val FILE_NAME = "setting_preference"

    private val KEY_INIT = "key_init"

    private val KEY_THEME = "key_theme"
    val THEME_LIGHT = 0
    val THEME_DARK = 1

    private val KEY_OPEN_URL = "key_open_url"
    val OPEN_URL_WEB_VIEW = 0
    val OPEN_URL_BROWSER = 1

    private val KEY_DATA_SOURCE = "key_data_source"
    val DATA_SOURCE_LOCAL = 0
    val DATA_SOURCE_REMOTE = 1

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    }

    fun isInit(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(KEY_INIT, false)
    }

    fun setInit(context: Context, init: Boolean) {
        getSharedPreferences(context).edit().putBoolean(KEY_INIT, init).apply()
    }

    fun getTheme(context: Context): Int {
        return getSharedPreferences(context).getInt(KEY_THEME, THEME_LIGHT)
    }

    fun setTheme(context: Context, theme: Int) {
        getSharedPreferences(context).edit().putInt(KEY_THEME, theme).apply()
    }

    fun getOpenUrl(context: Context): Int {
        return getSharedPreferences(context).getInt(KEY_OPEN_URL, OPEN_URL_WEB_VIEW)
    }

    fun setOpenUrl(context: Context, openUrl: Int) {
        getSharedPreferences(context).edit().putInt(KEY_OPEN_URL, openUrl).apply()
    }

    fun getDataSource(context: Context): Int {
        return getSharedPreferences(context).getInt(KEY_DATA_SOURCE, DATA_SOURCE_REMOTE)
    }

    fun setDataSource(context: Context, dataSource: Int) {
        getSharedPreferences(context).edit().putInt(KEY_DATA_SOURCE, dataSource).apply()
    }
}
