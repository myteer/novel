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