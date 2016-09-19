package com.liuqiang.gankforandroid.model.entity

import com.alibaba.fastjson.annotation.JSONField

class Result {

    class TypeData {
        @JSONField(name = "error") var error: Boolean = false
        @JSONField(name = "results") var results: List<Gank> = listOf()
    }

    class HistoryData {
        @JSONField(name = "error") var error: Boolean = false
        @JSONField(name = "results") var results: List<String> = listOf()
    }

    class DailyData {
        @JSONField(name = "error") var error: Boolean = false
        @JSONField(name = "category") var category: List<String> = listOf()
        @JSONField(name = "results") var results: Results = Results()

        class Results {
            @JSONField(name = "Android") var android: List<Gank>? = null
            @JSONField(name = "iOS") var ios: List<Gank>? = null
            @JSONField(name = "前端") var frontEnd: List<Gank>? = null
            @JSONField(name = "拓展资源") var resources: List<Gank>? = null
            @JSONField(name = "瞎推荐") var recommend: List<Gank>? = null
            @JSONField(name = "福利") var girl: List<Gank>? = null
            @JSONField(name = "休息视频") var video: List<Gank>? = null
        }
    }
}
