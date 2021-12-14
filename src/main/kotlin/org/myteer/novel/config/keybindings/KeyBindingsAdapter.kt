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
package org.myteer.novel.config.keybindings

import com.google.gson.*
import javafx.scene.input.KeyCodeCombination
import org.myteer.novel.config.ConfigAdapter
import org.myteer.novel.gui.keybinding.KeyBinding
import org.myteer.novel.gui.keybinding.KeyBindings
import org.slf4j.LoggerFactory
import java.lang.reflect.Type

class KeyBindingsAdapter : ConfigAdapter<KeyBindings> {
    override fun serialize(src: KeyBindings?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return src?.let { kbs ->
            JsonObject().apply {
                kbs.javaClass.declaredFields
                    .filter { it.type == KeyBinding::class.java }
                    .map { it.apply { isAccessible = true }.get(kbs) }
                    .map { it as KeyBinding }
                    .forEach { kb ->
                        kb.takeIf { it.keyCombination != it.defaultKeyCombination }?.let {
                            logger.debug("Writing key combination for '{}': '{}'...", it.id, it.keyCombination)
                            this.addProperty(it.id, it.keyCombination.toString())
                        }
                    }
            }
        } ?: JsonNull.INSTANCE
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): KeyBindings {
        return KeyBindings.apply {
            this.javaClass.declaredFields
                .filter { it.type == KeyBinding::class.java }
                .forEach { field ->
                    field.apply { isAccessible = true }
                        .get(this)
                        .let { it as KeyBinding }
                        .let { kb ->
                            try {
                                kb.keyCombinationProperty.set(
                                    json?.asJsonObject?.get(kb.id)?.asString?.let {
                                        logger.debug("Parsing key combination for '{}': '{}'...", kb.id, it)
                                        KeyCodeCombination.valueOf(it)
                                    } ?: kb.defaultKeyCombination
                                )
                            } catch (e: Exception) {
                                logger.error("Couldn't parse key binding", e)
                            }
                        }
                }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(KeyBindingsAdapter::class.java)
    }
}