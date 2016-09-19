package com.liuqiang.gankforandroid.view.activity.base

import com.liuqiang.gankforandroid.presenter.base.SimpleBasePresenter

/**
 * 为没有Presenter的滑动退出Activity提供的简单基类
 */
open class SimpleSwipeBackActivity :
        SwipeBackActivity<SimpleBasePresenter, SimpleBasePresenter.View>(), SimpleBasePresenter.View {

    override fun doInject() {
        activityComponent.inject(this)
    }
}