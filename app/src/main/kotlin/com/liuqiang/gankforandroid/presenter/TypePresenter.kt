package com.liuqiang.gankforandroid.presenter

import com.liuqiang.gankforandroid.model.db.GankDataService
import com.liuqiang.gankforandroid.model.entity.Type
import com.liuqiang.gankforandroid.presenter.base.BasePresenter
import javax.inject.Inject

class TypePresenter @Inject constructor(
        val gankDataService: GankDataService
) : BasePresenter<TypePresenter.View>() {

    interface View : BasePresenter.View {

        fun showTypes(types: List<Type>?)
    }

    var types: List<Type>? = null; private set

    fun loadVisibleTypes() {
        gankDataService
                .getVisibleTypes()
                .doAsyncAndChangeBaseLoading()
                .subscribeAndShowBaseError {
                    types = it
                    view?.showTypes(types)
                }.unsubscribeOnStop()
    }
}
