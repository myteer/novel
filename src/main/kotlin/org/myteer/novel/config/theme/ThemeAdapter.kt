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