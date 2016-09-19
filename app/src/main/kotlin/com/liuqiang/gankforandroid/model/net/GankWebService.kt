package com.liuqiang.gankforandroid.model.net

import com.liuqiang.gankforandroid.model.entity.Result
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

interface GankWebService {

    @GET("/api/day/history")
    fun getHistoryData(): Observable<Result.HistoryData>

    @GET("/api/day/{year}/{month}/{day}")
    fun getDailyData(@Path("year") year: Int, @Path("month") month: Int, @Path("day") day: Int): Observable<Result.DailyData>

    @GET("/api/data/{type}/{limit}/{page}")
    fun getTypeData(@Path("type") type: String, @Path("limit") limit: Int, @Path("page") page: Int): Observable<Result.TypeData>
}
