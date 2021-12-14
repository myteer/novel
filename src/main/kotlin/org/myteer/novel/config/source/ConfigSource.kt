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
package org.myteer.novel.config.source

import org.myteer.novel.config.PreferenceKey

interface ConfigSource {
    fun getInt(key: String, defaultValue: Int): Int

    fun getDouble(key: String, defaultValue: Double): Double

    fun getBoolean(key: String, defaultValue: Boolean): Boolean

    fun getString(key: String, defaultValue: String): String

    fun <T> get(key: PreferenceKey<T>): T

    fun putInt(key: String, value: Int)

    fun putDouble(key: String, value: Double)

    fun putBoolean(key: String, value: Boolean)

    fun putString(key: String, value: String)

    fun <T> put(key: PreferenceKey<T>, value: T)

    fun remove(key: String)

    fun remove(key: PreferenceKey<*>)

    fun reset()

    fun commit()

    fun isCreated(): Boolean
}