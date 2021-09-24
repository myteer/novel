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