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
package org.myteer.novel.config.theme

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import org.myteer.novel.config.ConfigAdapter
import org.myteer.novel.gui.theme.Theme
import java.lang.reflect.Type

class ThemeAdapter : ConfigAdapter<Theme> {
    private val serializer = ThemeSerializer()
    private val deserializer = ThemeDeserializer()

    override fun serialize(src: Theme?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return serializer.serialize(src, typeOfSrc, context)
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Theme {
        return deserializer.deserialize(json, typeOfT, context)
    }
}