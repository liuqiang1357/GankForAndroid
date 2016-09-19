package com.liuqiang.gankforandroid.view.activity.base

import android.os.Bundle
import android.view.View
import com.liuqiang.gankforandroid.presenter.base.BasePresenter
import me.imid.swipebacklayout.lib.SwipeBackLayout
import me.imid.swipebacklayout.lib.Utils
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper

/**
 * 滑动退出Activity，实现了保留实例对象，注入并管理Presenter等功能
 */
open class SwipeBackActivity<P : BasePresenter<V>, V : BasePresenter.View> :
        BaseActivity<P, V>(), SwipeBackActivityBase {

    private lateinit var mHelper: SwipeBackActivityHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHelper = SwipeBackActivityHelper(this)
        mHelper.onActivityCreate()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mHelper.onPostCreate()
    }

    override fun findViewById(id: Int): View {
        return super.findViewById(id) ?: mHelper.findViewById(id)
    }

    override fun getSwipeBackLayout(): SwipeBackLayout {
        return mHelper.swipeBackLayout
    }

    override fun setSwipeBackEnable(enable: Boolean) {
        swipeBackLayout.setEnableGesture(enable)
    }

    override fun scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this)
        swipeBackLayout.scrollToFinishActivity()
    }
}
