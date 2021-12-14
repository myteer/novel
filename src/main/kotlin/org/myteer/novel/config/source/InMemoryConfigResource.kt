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

class InMemoryConfigResource : ConfigSource {
    private val map = mutableMapOf<String, Any>()

    override fun getInt(key: String, defaultValue: Int): Int {
        return map[key] as? Int ?: defaultValue
    }

    override fun getDouble(key: String, defaultValue: Double): Double {
        return map[key] as? Double ?: defaultValue
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return map[key] as? Boolean ?: defaultValue
    }

    override fun getString(key: String, defaultValue: String): String {
        return map[key] as? String ?: defaultValue
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> get(key: PreferenceKey<T>): T {
        return map[key.jsonKey] as? T ?: key.defaultValue.get()
    }

    override fun putInt(key: String, value: Int) {
        map[key] = value
    }

    override fun putDouble(key: String, value: Double) {
        map[key] = value
    }

    override fun putBoolean(key: String, value: Boolean) {
        map[key] = value
    }

    override fun putString(key: String, value: String) {
        map[key] = value
    }

    override fun <T> put(key: PreferenceKey<T>, value: T) {
        map[key.jsonKey] = value!!
    }

    override fun remove(key: String) {
        map.remove(key)
    }

    override fun remove(key: PreferenceKey<*>) {
        map.remove(key.jsonKey)
    }

    override fun reset() = map.clear()

    override fun commit() {
    }

    override fun isCreated(): Boolean = true
}