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
import org.slf4j.LoggerFactory
import java.io.File

open class JsonFileConfigSource(private val file: File) : JsonConfigSource() {
    companion object {
        private val logger = LoggerFactory.getLogger(JsonFileConfigSource::class.java)
    }
    private val gson = Gson()
    private val created: Boolean
    private val jsonBase: JsonObject

    init {
        created = createIfNotExists(file)
        jsonBase = readJsonBase(file)
    }

    private fun createIfNotExists(file: File): Boolean {
        return try {
            file.parentFile.mkdirs()
            file.createNewFile()
        } catch (e: Exception) {
            logger.error("couldn't create file", e)
            false
        }
    }

    private fun readJsonBase(file: File): JsonObject {
        return try {
            val content = file.readText(Charsets.UTF_8)
            gson.fromJson(content, JsonObject::class.java) ?: JsonObject()
        } catch (e: Exception) {
            logger.error("couldn't read file", e)
            JsonObject()
        }
    }

    override fun getJsonBase(): JsonObject {
        return jsonBase
    }

    override fun commit() {
        val content = gson.toJson(jsonBase)
        file.writeText(content, Charsets.UTF_8)
    }

    override fun isCreated(): Boolean {
        return created
    }

    override fun reset() {
        super.reset()
        file.deleteOnExit()
    }
}