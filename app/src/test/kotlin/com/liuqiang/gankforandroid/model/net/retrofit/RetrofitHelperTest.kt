package com.liuqiang.gankforandroid.model.net.retrofit

import com.alibaba.fastjson.JSON
import com.liuqiang.gankforandroid.model.net.GankWebService
import org.junit.Before
import org.junit.Test

class RetrofitHelperTest {

    lateinit var gankWebService: GankWebService

    @Before
    fun setUp() {
        gankWebService = RetrofitHelper.getGankWebService()
    }

    @Test
    fun testGetHistoryData() {
        val historyData = gankWebService.getHistoryData().toBlocking().single()
        println(JSON.toJSON(historyData))
    }

    @Test
    fun testGetDailyData() {
        val dailyData = gankWebService.getDailyData(2015, 8, 7).toBlocking().single()
        println(JSON.toJSON(dailyData))
    }

    @Test
    fun testGetTypeData() {
        val typeData = gankWebService.getTypeData("Android", 20, 1).toBlocking().single()
        println(JSON.toJSON(typeData))
    }
}