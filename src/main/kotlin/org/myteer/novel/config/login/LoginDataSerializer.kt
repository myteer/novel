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
import java.lang.reflect.Type

class LoginDataSerializer : JsonSerializer<LoginData> {
    companion object {
        private const val SAVED_DATABASES = "svdbs"
        private const val SELECTED_DATABASE_INDEX = "sltdbi"
        private const val AUTO_LOGIN = "autolgn"
        private const val AUTO_LOGIN_CREDENTIALS = "lgncdt"
    }

    override fun serialize(src: LoginData?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return if (null == src) {
            JsonNull.INSTANCE
        } else {
            buildJsonObject(src)
        }
    }

    private fun buildJsonObject(src: LoginData): JsonObject {
        val gson = buildGson()
        return JsonObject().apply {
            add(SAVED_DATABASES, gson.toJsonTree(src.getSavedDatabases()))
            addProperty(SELECTED_DATABASE_INDEX, src.getSelectedDatabaseIndex())
            addProperty(AUTO_LOGIN, src.isAutoLogin())
            if (src.isAutoLogin()) {
                add(AUTO_LOGIN_CREDENTIALS, gson.toJsonTree(src.getAutoLoginCredentials()))
            }
        }
    }

    private fun buildGson(): Gson {
        return GsonBuilder().registerTypeAdapter(Credentials::class.java, CredentialsSerializer()).create()
    }
}