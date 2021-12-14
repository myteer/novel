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
package org.myteer.novel.gui.control

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.event.Event
import javafx.scene.control.TextField
import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyEvent
import org.myteer.novel.gui.utils.asKeyCombination
import org.myteer.novel.gui.utils.isUndefined

class KeyBindingDetectionField(initial: KeyCombination) : TextField() {
    private val keyCombination: ObjectProperty<KeyCombination> = object : SimpleObjectProperty<KeyCombination>() {
        override fun invalidated() {
            this@KeyBindingDetectionField.text = get()?.displayText?.takeIf { it.isNotEmpty() }
        }
    }

    init {
        isEditable = false
        keyCombination.set(initial)
        setOnContextMenuRequested(Event::consume)
        setOnKeyTyped(KeyEvent::consume)
        setOnKeyPressed { event ->
            event.consume()
            if (!event.isUndefined()) {
                event.asKeyCombination()?.let {
                    keyCombination.set(it)
                }
            }
        }
    }

    fun keyCombinationProperty() = keyCombination
}