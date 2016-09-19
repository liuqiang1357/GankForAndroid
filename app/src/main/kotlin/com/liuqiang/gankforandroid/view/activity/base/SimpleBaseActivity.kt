package com.liuqiang.gankforandroid.view.activity.base

import com.liuqiang.gankforandroid.presenter.base.SimpleBasePresenter

/**
 * 为没有Presenter的Activity提供的简单基类
 */
open class SimpleBaseActivity :
        BaseActivity<SimpleBasePresenter, SimpleBasePresenter.View>(), SimpleBasePresenter.View {

    override fun doInject() {
        activityComponent.inject(this)
    }
}