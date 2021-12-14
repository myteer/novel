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
package org.myteer.novel.config.login

import com.google.gson.*
import org.myteer.novel.db.DatabaseMeta
import java.lang.reflect.Type
import java.util.stream.Collectors
import java.util.stream.StreamSupport

class LoginDataDeserializer : JsonDeserializer<LoginData> {
    companion object {
        private const val SAVED_DATABASES = "svdbs"
        private const val SELECTED_DATABASE_INDEX = "sltdbi"
        private const val AUTO_LOGIN = "autolgn"
        private const val AUTO_LOGIN_CREDENTIALS = "lgncdt"
    }

    private val gson = buildGson()

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LoginData {
        return json?.let { buildLoginData(json.asJsonObject) } ?: LoginData.empty()
    }

    private fun buildLoginData(json: JsonObject): LoginData {
        return LoginData(getSavedDatabases(json)).apply {
            json.get(SELECTED_DATABASE_INDEX)?.asInt?.let {
                setSelectedDatabaseIndex(it)
            }
            json.get(AUTO_LOGIN)?.asBoolean?.let {
                setAutoLogin(it)
            }
            json.get(AUTO_LOGIN_CREDENTIALS)?.toString()?.let {
                setAutoLoginCredentials(gson.fromJson(it, Credentials::class.java))
            }
        }
    }

    private fun getSavedDatabases(json: JsonObject): List<DatabaseMeta> {
        return json.getAsJsonArray(SAVED_DATABASES)?.let {
            StreamSupport.stream(it.spliterator(), false)
                .map { item ->
                    gson.fromJson(item, DatabaseMeta::class.java)
                }
                .collect(Collectors.toList())
        } ?: listOf()
    }

    private fun buildGson(): Gson {
        return GsonBuilder().registerTypeAdapter(Credentials::class.java, CredentialsDeserializer()).create()
    }
}