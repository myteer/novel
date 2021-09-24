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