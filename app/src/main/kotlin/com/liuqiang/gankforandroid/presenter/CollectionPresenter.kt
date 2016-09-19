package com.liuqiang.gankforandroid.presenter

import com.liuqiang.gankforandroid.model.db.GankDataService
import com.liuqiang.gankforandroid.model.entity.Bookmark
import com.liuqiang.gankforandroid.presenter.base.BasePresenter
import javax.inject.Inject

class CollectionPresenter @Inject constructor(
        val gankDataService: GankDataService
) : BasePresenter<CollectionPresenter.View>() {

    interface View : BasePresenter.View {

        fun showBookmarks(bookmarks: List<Bookmark>?)
    }

    var bookmarks: List<Bookmark>? = null; private set

    fun loadAllBookmarks() {
        gankDataService
                .getAllBookmarks()
                .doAsyncAndChangeBaseLoading()
                .subscribeAndShowBaseError {
                    bookmarks = it
                    view?.showBookmarks(bookmarks)
                }.unsubscribeOnStop()
    }

    fun loadBookmarksByType(type: String) {
        gankDataService
                .getBookmarksByType(type)
                .doAsyncAndChangeBaseLoading()
                .subscribeAndShowBaseError {
                    bookmarks = it
                    view?.showBookmarks(bookmarks)
                }.unsubscribeOnStop()
    }
}
