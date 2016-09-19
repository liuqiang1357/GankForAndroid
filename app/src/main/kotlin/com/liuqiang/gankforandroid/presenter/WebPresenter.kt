package com.liuqiang.gankforandroid.presenter

import com.liuqiang.gankforandroid.model.db.GankDataService
import com.liuqiang.gankforandroid.model.entity.Bookmark
import com.liuqiang.gankforandroid.presenter.base.BasePresenter
import javax.inject.Inject

class WebPresenter @Inject constructor(
        val gankDataService: GankDataService
) : BasePresenter<WebPresenter.View>() {

    interface View : BasePresenter.View {

        fun showIsCollected(isCollected: Boolean?)
    }

    var isCollected: Boolean? = null; private set

    fun loadBookmark(objectId: String) {
        gankDataService
                .getBookmarkByObjectId(objectId)
                .doAsync()
                .subscribeAndShowBaseError {
                    isCollected = it != null
                    view?.showIsCollected(isCollected)
                }.unsubscribeOnStop()
    }

    fun addBookmark(bookmark: Bookmark) {
        gankDataService
                .addBookmark(bookmark)
                .doAsync()
                .subscribeAndShowBaseError {
                    isCollected = if (it > 0) true else isCollected
                    view?.showIsCollected(isCollected)
                }.unsubscribeOnStop()
    }

    fun removeBookmark(objectId: String) {
        gankDataService
                .removeBookmarkByObjectId(objectId)
                .doAsync()
                .subscribeAndShowBaseError {
                    isCollected = if (it > 0) false else isCollected
                    view?.showIsCollected(isCollected)
                }.unsubscribeOnStop()
    }
}
