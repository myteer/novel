package org.myteer.novel.utils

import java.util.*

class InMemoryResourceBundle private constructor(private val map: Map<String, String>) : ResourceBundle() {
    override fun handleGetObject(key: String) = map[key]

    override fun getKeys(): Enumeration<String> = Collections.enumeration(map.keys)

    class Builder {
        private val map: MutableMap<String, String> = mutableMapOf()

        fun put(key: String, value: String) = apply { map[key] = value }

        fun build() = InMemoryResourceBundle(map)
    }
}