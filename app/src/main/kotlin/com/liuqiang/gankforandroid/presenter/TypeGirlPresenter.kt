package com.liuqiang.gankforandroid.presenter

import com.liuqiang.gankforandroid.model.db.GankDataService
import com.liuqiang.gankforandroid.model.entity.Image
import com.liuqiang.gankforandroid.model.entity.Result
import com.liuqiang.gankforandroid.model.entity.Type
import com.liuqiang.gankforandroid.model.net.GankWebService
import com.liuqiang.gankforandroid.model.net.ImageWebService
import com.liuqiang.gankforandroid.presenter.base.BasePresenter
import com.liuqiang.gankforandroid.util.GANK_TYPE_DATA_LIMIT
import com.liuqiang.gankforandroid.util.TypeUtils
import rx.Observable
import rx.Subscription
import rx.schedulers.Schedulers
import rx.subscriptions.Subscriptions
import javax.inject.Inject

class TypeGirlPresenter @Inject constructor(
        val gankWebService: GankWebService,
        val imageWebService: ImageWebService,
        val gankDataService: GankDataService
) : BasePresenter<TypeGirlPresenter.View>() {

    interface View : BasePresenter.View {

        fun showImages(images: List<Image>?)

        fun showMoreImages(images: List<Image>?, start: Int, count: Int)
    }

    var images: List<Image>? = null

    var loadTypeData: Subscription = Subscriptions.unsubscribed(); private set
    var loadMoreTypeData: Subscription = Subscriptions.unsubscribed(); private set

    private var page: Int = 0
    private var allLoaded: Boolean = false

    fun loadTypeData(type: Type) {
        loadTypeData.unsubscribe()
        loadTypeData = gankWebService
                .getTypeData(TypeUtils.getRequestString(type.type), GANK_TYPE_DATA_LIMIT, 1)
                .map { typeDataToImages(it) }
                .doAsyncAndChangeBaseLoading()
                .subscribeAndShowBaseError {
                    images = it
                    page = 1
                    allLoaded = false
                    view?.showImages(images)
                }.unsubscribeOnStop()
    }

    fun loadMoreTypeData(type: Type) {
        if (allLoaded) {
            return
        }
        loadMoreTypeData.unsubscribe()
        loadMoreTypeData = gankWebService
                .getTypeData(TypeUtils.getRequestString(type.type), GANK_TYPE_DATA_LIMIT, page + 1)
                .map { typeDataToImages(it) }
                .doAsyncAndChangeBaseLoading()
                .subscribeAndShowBaseError {
                    val images = images?.toMutableList() ?: mutableListOf()
                    val start = images.size
                    val count = it.size
                    images.addAll(it)
                    this.images = images
                    page++
                    allLoaded = count < GANK_TYPE_DATA_LIMIT
                    view?.showMoreImages(this.images, start, count)
                }.unsubscribeOnStop()
    }

    private fun typeDataToImages(typeData: Result.TypeData): List<Image> {
        return typeData.results.map { it.toImage() }
                .let { gankDataService.getImages(it).toBlocking().single() }
                .map {
                    if (it.width == 0 || it.height == 0) {
                        imageWebService.getImageSize(it).subscribeOn(Schedulers.io())
                                .toBlocking().toFuture()
                    } else Observable.just(it).toBlocking().toFuture()
                }
                .map { it.get() }
                .apply { gankDataService.addImages(this).toBlocking().single() }
    }
}
