package com.liuqiang.gankforandroid.presenter

import com.liuqiang.gankforandroid.model.net.GankWebService
import com.liuqiang.gankforandroid.presenter.base.BasePresenter
import org.joda.time.LocalDate
import rx.Subscription
import rx.subscriptions.Subscriptions
import javax.inject.Inject

class DailyPresenter @Inject constructor(
        val gankWebService: GankWebService
) :BasePresenter<DailyPresenter.View>() {

    interface View : BasePresenter.View {

        fun showDates(dates: List<LocalDate>?)
    }

    var dates: List<LocalDate>? = null; private set

    var loadHistoryData: Subscription = Subscriptions.unsubscribed(); private set

    fun loadHistoryData() {
        loadHistoryData.unsubscribe()
        loadHistoryData = gankWebService
                .getHistoryData()
                .map { it.results.map { LocalDate(it) } }
                .doAsyncAndChangeBaseLoading()
                .subscribeAndShowBaseError {
                    dates = it
                    view?.showDates(dates)
                }.unsubscribeOnStop()
    }
}
