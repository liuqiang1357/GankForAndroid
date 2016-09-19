package com.liuqiang.gankforandroid.presenter

import com.liuqiang.gankforandroid.model.entity.Gank
import com.liuqiang.gankforandroid.model.entity.Type
import com.liuqiang.gankforandroid.model.net.GankWebService
import com.liuqiang.gankforandroid.presenter.base.BasePresenter
import com.liuqiang.gankforandroid.util.GANK_TYPE_DATA_LIMIT
import com.liuqiang.gankforandroid.util.TypeUtils
import rx.Subscription
import rx.subscriptions.Subscriptions
import javax.inject.Inject

class TypeGankPresenter @Inject constructor(
        val gankWebService: GankWebService
) : BasePresenter<TypeGankPresenter.View>() {

    interface View : BasePresenter.View {

        fun showGanks(ganks: List<Gank>?)

        fun showMoreGanks(ganks: List<Gank>?, start: Int, count: Int)
    }

    var ganks: List<Gank>? = null; private set

    var loadTypeData: Subscription = Subscriptions.unsubscribed(); private set
    var loadMoreTypeData: Subscription = Subscriptions.unsubscribed(); private set

    private var page: Int = 0
    private var allLoaded: Boolean = false

    fun loadTypeData(type: Type) {
        loadTypeData.unsubscribe()
        loadTypeData = gankWebService
                .getTypeData(TypeUtils.getRequestString(type.type), GANK_TYPE_DATA_LIMIT, 1)
                .map { it.results }
                .doAsyncAndChangeBaseLoading()
                .subscribeAndShowBaseError {
                    ganks = it
                    page = 1
                    allLoaded = false
                    view?.showGanks(ganks)
                }.unsubscribeOnStop()
    }

    fun loadMoreTypeData(type: Type) {
        if (allLoaded) {
            return
        }
        loadMoreTypeData.unsubscribe()
        loadMoreTypeData = gankWebService
                .getTypeData(TypeUtils.getRequestString(type.type), GANK_TYPE_DATA_LIMIT, page + 1)
                .map { it.results }
                .doAsyncAndChangeBaseLoading()
                .subscribeAndShowBaseError {
                    val ganks = ganks?.toMutableList() ?: mutableListOf()
                    val start = ganks.size
                    val count = it.size
                    ganks.addAll(it)
                    this.ganks = ganks
                    page++
                    allLoaded = count < GANK_TYPE_DATA_LIMIT
                    view?.showMoreGanks(this.ganks, start, count)
                }.unsubscribeOnStop()
    }
}
