package com.liuqiang.gankforandroid.util

import com.liuqiang.gankforandroid.BuildConfig
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(23))
@FixMethodOrder(MethodSorters.JVM)
class SettingUtilsTest {

    @Test
    fun testInit() {
        val context = RuntimeEnvironment.application

        SettingUtils.setInit(context, true)
        assertEquals(true, SettingUtils.isInit(context))

        SettingUtils.setInit(context, false)
        assertEquals(false, SettingUtils.isInit(context))
    }

    @Test
    fun testOpenUrl() {
        val context = RuntimeEnvironment.application

        SettingUtils.setOpenUrl(context, SettingUtils.OPEN_URL_BROWSER)
        assertEquals(SettingUtils.OPEN_URL_BROWSER, SettingUtils.getOpenUrl(context))

        SettingUtils.setOpenUrl(context, SettingUtils.OPEN_URL_WEB_VIEW)
        assertEquals(SettingUtils.OPEN_URL_WEB_VIEW, SettingUtils.getOpenUrl(context))
    }
}
