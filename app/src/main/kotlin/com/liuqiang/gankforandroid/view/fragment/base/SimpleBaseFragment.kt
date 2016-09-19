package com.liuqiang.gankforandroid.view.fragment.base

import com.liuqiang.gankforandroid.presenter.base.SimpleBasePresenter

/**
 * 为没有Presenter的Fragment提供的简单基类
 */
open class SimpleBaseFragment:
        BaseFragment<SimpleBasePresenter, SimpleBasePresenter.View>(), SimpleBasePresenter.View {

    override fun doInject() {
        fragmentComponent.inject(this)
    }
}
