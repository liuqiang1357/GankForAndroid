package com.liuqiang.gankforandroid.util

import org.junit.Test
import kotlin.test.assertEquals

class KotlinUtilsTest {

    @Test
    fun test() {
        var c = 0
        val c1 = noNullOf(c)

        testClosure({ c }, { c = it })
        assertEquals(100, c)
        println(c)

        testSetter(setterOf({ c }, { c = it }))
        assertEquals(200, c)
        println(c)

        testNoNull(c1)
        assertEquals(300, c1.value)
        println(c1.value)
    }

    fun testClosure(getter: () -> Int, setter: (Int) -> Unit) {
        println(getter.invoke())
        setter.invoke(100)
    }

    fun testSetter(c: Setter<Int>) {
        println(c.get())
        c.set(200)
    }

    fun testNoNull(c: NoNull<Int>) {
        println(c.value)
        c.value = 300
    }
}