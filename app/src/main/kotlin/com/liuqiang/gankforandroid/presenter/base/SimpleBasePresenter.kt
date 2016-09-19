package com.liuqiang.gankforandroid.presenter.base

import javax.inject.Inject

/**
 * 为不需要Presenter的View提供的简单基类
 */
class SimpleBasePresenter @Inject constructor(): BasePresenter<SimpleBasePresenter.View>(){

    interface View : BasePresenter.View
}
