package com.liuqiang.gankforandroid.presenter.base

import android.support.annotation.CallSuper
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

/**
 * Presenter基类，实现了attach/detach View，start/stop，显示加载中，显示错误等功能
 */
open class BasePresenter<V : BasePresenter.View> : Presenter() {

    interface View : Presenter.View {

        /**
         * 显示加载中
         */
        fun showBaseLoading(loading: Boolean)

        /**
         * 显示错误
         */
        fun showBaseError(throwable: Throwable)
    }

    /**
     * 当前Presenter所绑定的View
     */
    var view: V? = null; private set

    /**
     * 绑定View
     */
    @CallSuper
    open fun attachView(view: V) {
        this.view = view
    }

    /**
     * 脱离View
     */
    @CallSuper
    open fun detachView() {
        view = null
    }

    /**
     * 是否已启动
     */
    var isStarted: Boolean = false; private set

    /**
     * 等待取消的异步任务
     */
    private val subscriptions: CompositeSubscription = CompositeSubscription()

    /**
     * 启动
     */
    @CallSuper
    open fun start() {
        isStarted = true
    }

    /**
     * 停止，并取消正在执行的任务
     */
    @CallSuper
    open fun stop() {
        isStarted = false
        subscriptions.unsubscribe()
    }

    /**
     * 是否在加载中
     */
    val isBaseLoading: Boolean get() = baseLoadingCount > 0

    /**
     * 当前的BaseLoading状态，值为正在Loading的任务数量
     */
    private var baseLoadingCount: Int = 0

    /**
     * 改变BaseLoading的状态，并将汇总的结果反映到View上
     */
    protected fun changeBaseLoading(show: Boolean) {
        baseLoadingCount += if (show) 1 else -1
        view?.showBaseLoading(isBaseLoading)
    }

    /**
     * 显示BaseError
     */
    protected fun showBaseError(throwable: Throwable) {
        view?.showBaseError(throwable)
    }

    /**
     * 在IO线程执行并回调在主线程
     */
    protected fun <T> Observable<T>.doAsync(): Observable<T> {
        return subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 在IO线程执行并回调在主线程，并改变BaseLoading状态
     */
    protected fun <T> Observable<T>.doAsyncAndChangeBaseLoading(): Observable<T> {
        return subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { changeBaseLoading(true) }
                .doOnUnsubscribe { changeBaseLoading(false) }
    }

    /**
     * 订阅并在出错时显示BaseError
     */
    protected fun <T> Observable<T>.subscribeAndShowBaseError(onNext: (T) -> Unit): Subscription {
        return subscribe(onNext, { showBaseError(it) })
    }

    /**
     * 在stop时取消该任务
     */
    protected fun Subscription.unsubscribeOnStop(): Subscription {
        subscriptions.add(this)
        return this
    }
}
