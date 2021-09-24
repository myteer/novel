package org.myteer.novel.config

import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer

interface ConfigAdapter<T> : JsonSerializer<T>, JsonDeserializer<T> {
}