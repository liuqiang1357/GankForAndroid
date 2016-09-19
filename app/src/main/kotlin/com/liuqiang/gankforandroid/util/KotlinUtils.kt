@file:JvmName("Functions")
@file:JvmMultifileClass

package com.liuqiang.gankforandroid.util

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

data class NoNull<T>(var value: T)

fun <T> noNullOf(value: T): NoNull<T> = NoNull<T>(value)

data class Nullable<T>(var value: T? = null)

fun <T> nullableOf(value: T? = null): Nullable<T> = Nullable<T>(value)

open class Getter<out T>(private val getter: () -> T) {
    fun get(): T {
        return getter.invoke()
    }
}

fun <T> getterOf(getter: () -> T) = Getter<T>(getter)

class Setter<T>(getter: () -> T, private val setter: (value: T) -> Unit) : Getter<T>(getter) {
    fun set(value: T) {
        setter.invoke(value)
    }
}

fun <T> setterOf(getter: () -> T, setter: (value: T) -> Unit) = Setter<T>(getter, setter)

inline fun <T1 : Any, R> safeLet(p1: T1?, block: (T1) -> R): R? {
    return if (p1 != null) block(p1) else null
}

inline fun <T1 : Any, T2 : Any, R> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

inline fun <T1 : Any, T2 : Any, T3 : Any, R> safeLet(p1: T1?, p2: T2?, p3: T3?, block: (T1, T2, T3) -> R): R? {
    return if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
}

inline fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, R> safeLet(p1: T1?, p2: T2?, p3: T3?, p4: T4?, block: (T1, T2, T3, T4) -> R): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null) block(p1, p2, p3, p4) else null
}

inline fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, R> safeLet(p1: T1?, p2: T2?, p3: T3?, p4: T4?, p5: T5?, block: (T1, T2, T3, T4, T5) -> R): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null && p5 != null) block(p1, p2, p3, p4, p5) else null
}

inline fun <R> noThrow(block: () -> R): R? {
    try {
        return block()
    } catch (e: Throwable) {
        e.printStackTrace()
    }
    return null
}

fun ViewGroup.inflate(resource: Int, root: ViewGroup?, attachToRoot: Boolean): View {
    return LayoutInflater.from(this.context).inflate(resource, root, attachToRoot)
}

inline fun <reified T : Any> T.logd(msg: String) {
    Log.d(T::class.java.simpleName, msg)
}

inline fun <reified T : Any> T.loge(msg: String) {
    Log.e(T::class.java.simpleName, msg)
}
