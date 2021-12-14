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
package org.myteer.novel.gui.volume.chapter

import com.google.gson.*
import javafx.beans.property.*
import javafx.scene.paint.Color
import org.myteer.novel.config.ConfigAdapter
import org.myteer.novel.config.PreferenceKey
import java.lang.reflect.Type

class ChapterShowConfiguration {
    val fontFamilyProperty: StringProperty = SimpleStringProperty("")
    val fontSizeProperty: IntegerProperty = SimpleIntegerProperty(12)
    val fontColorProperty: ObjectProperty<Color> = SimpleObjectProperty(Color.BLACK)

    companion object {
        val CHAPTER_SHOW_CONFIG_KEY = PreferenceKey(
            "chapter.show.config",
            ChapterShowConfiguration::class.java,
            ChapterShowConfiguration::getDefault,
            ChapterShowConfigurationAdapter()
        )

        private val DEFAULT_INSTANCE = ChapterShowConfiguration()

        private fun getDefault() = DEFAULT_INSTANCE
    }

    private class ChapterShowConfigurationAdapter : ConfigAdapter<ChapterShowConfiguration> {
        companion object {
            private const val FONT_FAMILY = "font.family"
            private const val FONT_SIZE = "font.size"
            private const val FONT_COLOR = "font.color"
        }

        override fun serialize(
            src: ChapterShowConfiguration?,
            typeOfSrc: Type?,
            context: JsonSerializationContext?
        ): JsonElement {
            return src?.let {
                JsonObject().apply {
                    addProperty(FONT_FAMILY, it.fontFamilyProperty.value)
                    addProperty(FONT_SIZE, it.fontSizeProperty.value)
                    addProperty(FONT_COLOR, it.fontColorProperty.value.toString())
                }
            } ?: JsonNull.INSTANCE
        }

        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): ChapterShowConfiguration {
            return ChapterShowConfiguration().apply {
                json?.asJsonObject?.let {
                    it.get(FONT_FAMILY)?.asString?.let(fontFamilyProperty::set)
                    it.get(FONT_SIZE)?.asInt?.let(fontSizeProperty::set)
                    it.get(FONT_COLOR)?.asString?.let { color ->
                        fontColorProperty.set(Color.valueOf(color))
                    }
                }
            }
        }
    }
}