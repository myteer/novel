package org.myteer.novel.config.theme

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.myteer.novel.gui.theme.Theme
import org.slf4j.LoggerFactory
import java.lang.reflect.Type

class ThemeDeserializer : JsonDeserializer<Theme> {
    companion object {
        private val logger = LoggerFactory.getLogger(ThemeDeserializer::class.java)
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Theme {
        var theme: Theme? = null
        json?.let {
            try {
                val clazz = ThemeDeserializer::class.java.classLoader.loadClass(it.asString)
                if (Theme::class.java != clazz && Theme::class.java.isAssignableFrom(clazz)) {
                    theme = clazz.getConstructor().apply {
                        isAccessible = true
                    }.newInstance() as Theme
                }
            } catch (e: Exception) {
                logger.error("deserialize theme error, json: {}", json, e)
            }
        }
        return theme ?: Theme.getDefault()
    }
}