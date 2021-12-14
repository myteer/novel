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

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.myteer.novel.config.PreferenceKey

abstract class JsonConfigSource : ConfigSource {
    private val gson = Gson()

    protected abstract fun getJsonBase(): JsonObject

    override fun getInt(key: String, defaultValue: Int): Int {
        return getJsonBase().get(key)?.asInt ?: defaultValue
    }

    override fun getDouble(key: String, defaultValue: Double): Double {
        return getJsonBase().get(key)?.asDouble ?: defaultValue
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return getJsonBase().get(key)?.asBoolean ?: defaultValue
    }

    override fun getString(key: String, defaultValue: String): String {
        return getJsonBase().get(key)?.asString ?: defaultValue
    }

    override fun <T> get(key: PreferenceKey<T>): T {
        return getJsonBase().get(key.jsonKey)?.let {
            key.configAdapter?.deserialize(it, key.type, null) ?: gson.fromJson(it, key.type)
        } ?: key.defaultValue.get()
    }

    override fun putInt(key: String, value: Int) {
        getJsonBase().addProperty(key, value)
    }

    override fun putDouble(key: String, value: Double) {
        getJsonBase().addProperty(key, value)
    }

    override fun putBoolean(key: String, value: Boolean) {
        getJsonBase().addProperty(key, value)
    }

    override fun putString(key: String, value: String) {
        getJsonBase().addProperty(key, value)
    }

    override fun <T> put(key: PreferenceKey<T>, value: T) {
        val jsonElement = key.configAdapter?.serialize(value, key.type, null) ?: gson.toJsonTree(value, key.type)
        getJsonBase().add(key.jsonKey, jsonElement)
    }

    override fun remove(key: String) {
        getJsonBase().remove(key)
    }

    override fun remove(key: PreferenceKey<*>) {
        remove(key.jsonKey)
    }

    override fun reset() {
        val json = getJsonBase()
        val keys = HashSet(json.keySet())
        keys.forEach(json::remove)
    }
}