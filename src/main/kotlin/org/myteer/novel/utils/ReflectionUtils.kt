/*
 * Copyright (c) 2021 MTSoftware
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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