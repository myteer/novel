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
        json.keySet().forEach(json::remove)
    }
}