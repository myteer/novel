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