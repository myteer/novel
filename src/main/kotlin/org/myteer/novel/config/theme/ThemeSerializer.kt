package org.myteer.novel.config.theme

import com.google.gson.*
import org.myteer.novel.gui.theme.Theme
import java.lang.reflect.Type

class ThemeSerializer : JsonSerializer<Theme> {
    override fun serialize(src: Theme?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return src?.javaClass?.name?.let { JsonPrimitive(it) } ?: JsonNull.INSTANCE
    }
}