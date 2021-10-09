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