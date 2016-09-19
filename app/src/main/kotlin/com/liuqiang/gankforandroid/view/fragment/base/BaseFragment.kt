package com.liuqiang.gankforandroid.view.fragment.base

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.design.widget.Snackbar
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.liuqiang.gankforandroid.App
import com.liuqiang.gankforandroid.di.component.DaggerFragmentComponent
import com.liuqiang.gankforandroid.di.component.FragmentComponent
import com.liuqiang.gankforandroid.di.module.FragmentModule
import com.liuqiang.gankforandroid.presenter.base.BasePresenter
import com.liuqiang.gankforandroid.util.ErrorUtils
import com.liuqiang.gankforandroid.util.inflate
import com.liuqiang.gankforandroid.util.safeLet
import com.liuqiang.gankforandroid.view.activity.base.BaseActivity
import com.trello.rxlifecycle.components.support.RxFragment
import dagger.Lazy
import javax.inject.Inject

/**
 * Fragment基类，实现了保留实例对象，注入并管理Presenter等功能
 */
open class BaseFragment<P : BasePresenter<V>, V : BasePresenter.View> :
        RxFragment(), BasePresenter.View {

    companion object {
        private val KEY_PRESENTER = "BaseFragment:presenter"
    }

    /**
     * View的布局资源，如果没有重写，需要重写onCreateView创建View
     */
    open val layoutResourceId: Int = 0

    /**
     * 是否为重建View保留Presenter
     * （如Configuration改变，Activity recreate，Fragment重新attach时），
     * 如果启用，则在重建View时使用原来的Presenter，并执行refreshView；
     * 如不启用，则在重建View时创建新的Presenter，并且执行startPresenter
     */
    open var retainPresenter: Boolean = true

    /**
     * 获得这个Fragment所在的Activity，这个Activity需要继承自BaseActivity
     */
    val baseActivity: BaseActivity<*, *> get() = activity as BaseActivity<*, *>

    /**
     * 获得这个Fragment的父Fragment，如不存在则返回null
     */
    val parentBaseFragment: BaseFragment<*, *>? get() = parentFragment as? BaseFragment<*, *>

    /**
     * 重建时保留实例对象的容器
     */
    var retainedInstanceState: MutableMap<String, Any> = mutableMapOf(); private set

    /**
     * View是否完成初始化，即调用过initView且没有调用过finalizeView
     */
    var isViewInitialized: Boolean = false; private set

    /**
     * Fragment的依赖注入组件
     */
    lateinit var fragmentComponent: FragmentComponent; private set

    /**
     * 当前View所持有的Presenter实例
     */
    lateinit var presenter: P; private set

    /**
     * Presenter的注入位置，当Presenter保留时，不会获取新的Presenter
     */
    @Inject lateinit var presenterStub: Lazy<P>

    /**
     * 创建Fragment
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    /**
     * 创建View
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if(layoutResourceId == 0){
            throw AssertionError("layoutResourceId not provided")
        }
        if (container != null) {
            return container.inflate(layoutResourceId, container, false)
        }
        return inflater.inflate(layoutResourceId, container, false)
    }

    /**
     * 创建View完成回调，初始化View，并绑定Presenter
     */
    @Suppress("UNCHECKED_CAST")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentComponent = DaggerFragmentComponent.builder()
                .applicationComponent((activity.application as App).applicationComponent)
                .fragmentModule(FragmentModule(this))
                .build()
        doInject()

        if (retainedInstanceState[KEY_PRESENTER] != null) {
            presenter = retainedInstanceState[KEY_PRESENTER] as P
        } else {
            presenter = presenterStub.get()
        }

        initView(savedInstanceState)
        isViewInitialized = true
        presenter.attachView(this as V)

        if (presenter.isStarted) {
            refreshView()
        } else {
            presenter.start()
            startPresenter()
        }
    }

    /**
     * 销毁View回调，终止化View，并解绑Presenter
     */
    override fun onDestroyView() {
        super.onDestroyView()

        finalizeView()
        isViewInitialized = false
        presenter.detachView()

        onRetainInstanceState(retainedInstanceState)
        if (!retainPresenter) {
            presenter.stop()
        }
    }

    /**
     * 销毁Fragment
     */
    override fun onDestroy() {
        super.onDestroy()
        retainedInstanceState.clear()
        if (presenter.isStarted) {
            presenter.stop()
        }
    }

    /**
     * 重建时调用以保留实例对象
     * @param retainedInstanceState 保留实例对象的容器
     */
    @CallSuper
    open fun onRetainInstanceState(retainedInstanceState: MutableMap<String, Any>) {
        if (retainPresenter) {
            retainedInstanceState[KEY_PRESENTER] = presenter
        }
    }

    /**
     * 执行注入
     */
    open fun doInject() = Unit

    /**
     * 初始化View
     */
    open fun initView(savedInstanceState: Bundle?) = Unit

    /**
     * 终止化View
     */
    open fun finalizeView() = Unit

    /**
     * 启动Presenter
     */
    open fun startPresenter() = Unit

    /**
     * 刷新View
     */
    @CallSuper
    open fun refreshView() = showBaseLoading(presenter.isBaseLoading)

    /**
     * 按键事件，需要从Activity中传递过来
     */
    open fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return false
    }

    /**
     * 设置SupportActionBar，优先调用父Fragment的方法
     */
    @CallSuper
    open fun setSupportActionBar(toolbar: Toolbar) {
        parentBaseFragment?.setSupportActionBar(toolbar)
                ?: baseActivity.setSupportActionBar(toolbar)
    }

    /**
     * 获取SupportActionBar，优先调用父Fragment的方法
     */
    fun getSupportActionBar(): ActionBar? {
        return parentBaseFragment?.getSupportActionBar()
                ?: baseActivity.getSupportActionBar()
    }

    /**
     * 显示BaseLoading，实现自BasePresenter.View
     */
    override fun showBaseLoading(loading: Boolean) {
    }

    /**
     * 显示BaseError，实现自BasePresenter.View
     */
    override fun showBaseError(throwable: Throwable) {
        throwable.printStackTrace()
        safeLet(view) {
            val string = ErrorUtils.getLocalString(context, throwable)
            Snackbar.make(it, string, Snackbar.LENGTH_LONG).show()
        }
    }
}
