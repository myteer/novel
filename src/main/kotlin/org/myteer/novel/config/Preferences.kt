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
package org.myteer.novel.config

import org.myteer.novel.config.source.ConfigSource
import org.myteer.novel.config.source.DefaultConfigSource
import org.myteer.novel.config.source.InMemoryConfigResource
import org.slf4j.LoggerFactory

class Preferences(val source: ConfigSource) {
    companion object {
        private val logger = LoggerFactory.getLogger(Preferences::class.java)
        private var default: Preferences? = null

        @Synchronized
        fun getPreferences(): Preferences {
            if (null == default) {
                default = Preferences(DefaultConfigSource())
                logger.debug("Default preferences/config source set: '${default!!.source.javaClass.name}'")
            }
            return default!!
        }

        fun empty() = Preferences(InMemoryConfigResource())
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return source.getInt(key, defaultValue)
    }

    fun getDouble(key: String, defaultValue: Double): Double {
        return source.getDouble(key, defaultValue)
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return source.getBoolean(key, defaultValue)
    }

    fun getString(key: String, defaultValue: String): String {
        return source.getString(key, defaultValue)
    }

    fun <T> get(key: PreferenceKey<T>): T {
        return source.get(key)
    }

    fun editor() = Editor()

    inner class Editor {
        fun putInt(key: String, value: Int): Editor {
            source.putInt(key, value)
            return this
        }

        fun putDouble(key: String, value: Double): Editor {
            source.putDouble(key, value)
            return this
        }

        fun putBoolean(key: String, value: Boolean): Editor {
            source.putBoolean(key, value)
            return this
        }

        fun putString(key: String, value: String): Editor {
            source.putString(key, value)
            return this
        }

        fun <T> put(key: PreferenceKey<T>, value: T): Editor {
            source.put(key, value)
            return this
        }

        fun remove(key: String): Editor {
            source.remove(key)
            return this
        }

        fun remove(key: PreferenceKey<*>): Editor {
            source.remove(key)
            return this
        }

        fun reset() = source.reset()

        fun commit() = source.commit()

        fun tryCommit() {
            return try {
                commit()
            } catch (ignored: Exception) { }
        }
    }
}