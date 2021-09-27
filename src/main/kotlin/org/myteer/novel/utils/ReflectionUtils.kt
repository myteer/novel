@file:Suppress("UNCHECKED_CAST")

package org.myteer.novel.utils

import java.util.stream.Stream

object ReflectionUtils {
    fun <T> constructObject(clazz: Class<T>, args: Array<out Any>): T {
        val constructorParamTypes = Stream.of(*args).map {
            it.javaClass
        }.toArray() as Array<out Class<*>>
        val constructor = clazz.getDeclaredConstructor(*constructorParamTypes)
        constructor.isAccessible = true
        return constructor.newInstance()
    }

    fun <T> constructObject(clazz: Class<T>): T {
        val constructor = clazz.getDeclaredConstructor()
        constructor.isAccessible = true
        return constructor.newInstance()
    }

    fun <T> tryConstructObject(clazz: Class<T>): T? {
        return try {
            constructObject(clazz)
        } catch (ignored: Throwable) {
            null
        }
    }
}