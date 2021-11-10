package org.myteer.novel.utils

import com.google.gson.JsonElement
import com.google.gson.JsonObject

operator fun JsonObject.minus(key: String): JsonElement? = remove(key)

operator fun JsonObject.set(key: String, value: String) = addProperty(key, value)

operator fun JsonObject.set(key: String, value: Number) = addProperty(key, value)

operator fun JsonObject.set(key: String, value: Char) = addProperty(key, value)

operator fun JsonObject.set(key: String, value: Boolean) = addProperty(key, value)