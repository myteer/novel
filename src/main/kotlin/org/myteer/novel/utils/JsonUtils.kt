package org.myteer.novel.utils

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson

object JsonUtils {
    private val gson: Gson = Gson()
    private val mapper: ObjectMapper = ObjectMapper()

    init {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }

    fun toJson(obj: Any?): String {
        return try {
            mapper.writeValueAsString(obj)
        } catch (e: Exception) {
            gson.toJson(obj)
        }
    }

    fun <T> fromJson(value: String, valueType: Class<T>): T {
        return try {
            mapper.readValue(value, valueType)
        } catch (e: Exception) {
            gson.fromJson(value, valueType)
        }
    }

    fun <T> fromJsonForGeneric(value: String, typeReference: TypeReference<T>): T {
        return try {
            mapper.readValue(value, typeReference)
        } catch (e: Exception) {
            System.err.println(e)
            gson.fromJson(value, typeReference.type)
        }
    }
}