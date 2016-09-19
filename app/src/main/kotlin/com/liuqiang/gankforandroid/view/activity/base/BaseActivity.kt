package com.liuqiang.gankforandroid.view.activity.base

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.design.widget.Snackbar
import android.view.KeyEvent
import com.liuqiang.gankforandroid.App
import com.liuqiang.gankforandroid.di.component.ActivityComponent
import com.liuqiang.gankforandroid.di.component.DaggerActivityComponent
import com.liuqiang.gankforandroid.di.module.ActivityModule
import com.liuqiang.gankforandroid.presenter.base.BasePresenter
import com.liuqiang.gankforandroid.util.ErrorUtils
import com.liuqiang.gankforandroid.util.safeLet
import com.liuqiang.gankforandroid.view.fragment.base.BaseFragment
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import dagger.Lazy
import org.jetbrains.anko.contentView
import javax.inject.Inject

/**
 * Activity基类，实现了保留实例对象，注入并管理Presenter等功能
 */
open class BaseActivity<P : BasePresenter<V>, V : BasePresenter.View> :
        RxAppCompatActivity(), BasePresenter.View {

    companion object {
        private val KEY_PRESENTER = "BaseFragment:presenter"
    }

    /**
     * contentView的布局资源，如果没有重写，需要在initView中调用setContentView
     */
    open val layoutResourceId: Int = 0

    /**
     * theme资源，当需求切换主题时应该重写
     */
    open val themeResourceId: Int = 0

    /**
     * 是否为重建View保留Presenter
     * （如Configuration改变，Activity recreate，Fragment重新attach时），
     * 如果启用，则在重建View时使用原来的Presenter，并执行refreshView；
     * 如不启用，则在重建View时创建新的Presenter，并且执行startPresenter
     */
    open var retainPresenter: Boolean = true

    /**
     * 获得这个Activity的根Fragment，用来传递事件，如果没有，可以不重写
     */
    open val rootBaseFragment: BaseFragment<*, *>? get() = null

    /**
     * 重建时保留实例对象的容器
     */
    var retainedInstanceState: MutableMap<String, Any> = mutableMapOf(); private set

    /**
     * View是否完成初始化，即调用过initView且没有调用过finalizeView
     */
    var isViewInitialized: Boolean = false; private set

    /**
     * Activity的依赖注入组件
     */
    lateinit var activityComponent: ActivityComponent; private set

    /**
     * 当前View所持有的Presenter实例
     */
    lateinit var presenter: P; private set

    /**
     * Presenter的注入位置，当Presenter保留时，不会获取新的Presenter
     */
    @Inject lateinit var presenterStub: Lazy<P>

    /**
     * 创建Activity
     */
    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (themeResourceId != 0) {
            setTheme(themeResourceId)
        }
        if (layoutResourceId != 0) {
            setContentView(layoutResourceId)
        }
        safeLet(lastCustomNonConfigurationInstance as? MutableMap<String, Any>) {
            retainedInstanceState = it
        }

        activityComponent = DaggerActivityComponent.builder()
                .applicationComponent((application as App).applicationComponent)
                .activityModule(ActivityModule(this))
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
     * 销毁Activity
     */
    override fun onDestroy() {
        super.onDestroy()

        finalizeView()
        isViewInitialized = false
        presenter.detachView()

        if (!retainPresenter || lastCustomNonConfigurationInstance == null) {
            presenter.stop()
        }
    }

    /**
     * 保留实例对象， 禁止重写，需要时重写onRetainInstanceState
     */
    final override fun onRetainCustomNonConfigurationInstance(): Any {
        onRetainInstanceState(retainedInstanceState)
        return retainedInstanceState
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
     * 按键事件，如果有根Fragment，则会先交给根Fragment处理事件
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (rootBaseFragment?.onKeyDown(keyCode, event) ?: false) {
            return true
        }
        return super.onKeyDown(keyCode, event)
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
        safeLet(contentView) {
            val string = ErrorUtils.getLocalString(this, throwable)
            Snackbar.make(it, string, Snackbar.LENGTH_LONG).show()
        }
    }
}
