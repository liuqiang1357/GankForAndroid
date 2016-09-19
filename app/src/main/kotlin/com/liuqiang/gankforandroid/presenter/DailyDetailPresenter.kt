package com.liuqiang.gankforandroid.presenter

import com.liuqiang.gankforandroid.model.entity.Gank
import com.liuqiang.gankforandroid.model.entity.Image
import com.liuqiang.gankforandroid.model.entity.Result
import com.liuqiang.gankforandroid.model.net.GankWebService
import com.liuqiang.gankforandroid.presenter.base.BasePresenter
import com.liuqiang.gankforandroid.util.safeLet
import org.joda.time.LocalDate
import rx.Subscription
import rx.subscriptions.Subscriptions
import javax.inject.Inject

class DailyDetailPresenter @Inject constructor(
        val gankWebService: GankWebService
) : BasePresenter<DailyDetailPresenter.View>() {

    interface View : BasePresenter.View {

        fun showImagesAndGanks(images: List<Image>?, ganks: List<Gank>?)
    }

    var images: List<Image>? = null; private set
    var ganks: List<Gank>? = null; private set

    var loadDailyData: Subscription = Subscriptions.unsubscribed(); private set

    fun loadDailyData(date: LocalDate) {
        loadDailyData.unsubscribe()
        loadDailyData = gankWebService
                .getDailyData(date.year, date.monthOfYear, date.dayOfMonth)
                .map { dailyDataToImagesAndGanks(it) }
                .doAsyncAndChangeBaseLoading()
                .subscribeAndShowBaseError {
                    images = it.images
                    ganks = it.ganks
                    view?.showImagesAndGanks(images, ganks)
                }.unsubscribeOnStop()
    }

    private data class ImagesAndGanks(var images: List<Image>, var ganks: List<Gank>)

    private fun dailyDataToImagesAndGanks(dailyData: Result.DailyData): ImagesAndGanks {
        val images: MutableList<Image> = mutableListOf()
        safeLet(dailyData.results.girl) { images.addAll(it.map { it.toImage() }) }
        val ganks: MutableList<Gank> = mutableListOf()
        safeLet(dailyData.results.android) { ganks.addAll(it) }
        safeLet(dailyData.results.ios) { ganks.addAll(it) }
        safeLet(dailyData.results.frontEnd) { ganks.addAll(it) }
        safeLet(dailyData.results.resources) { ganks.addAll(it) }
        safeLet(dailyData.results.recommend) { ganks.addAll(it) }
        safeLet(dailyData.results.video) { ganks.addAll(it) }
        return ImagesAndGanks(images, ganks)
    }
}
