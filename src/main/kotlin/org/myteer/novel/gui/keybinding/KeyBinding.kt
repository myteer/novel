package org.myteer.novel.gui.keybinding

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyEvent
import org.myteer.novel.utils.os.OsInfo

class KeyBinding(
    val id: String,
    val title: String,
    val description: () -> String,
    val defaultKeyCombination: KeyCombination
) {
    val keyCombinationProperty: ObjectProperty<KeyCombination> = SimpleObjectProperty(defaultKeyCombination)
    val keyCombination: KeyCombination
        get() = keyCombinationProperty.get()

    constructor(
        id: String,
        title: String,
        description: () -> String,
        winLinuxKeyCombination: KeyCombination,
        macKeyCombination: KeyCombination
    ) : this(
        id,
        title,
        description,
        when {
            OsInfo.isMac() -> macKeyCombination
            else -> winLinuxKeyCombination
        }
    )

    constructor(
        id: String,
        title: String,
        description: () -> String,
        winKeyCombination: KeyCombination,
        linuxKeyCombination: KeyCombination,
        macKeyCombination: KeyCombination
    ) : this(
        id,
        title,
        description,
        when {
            OsInfo.isMac() -> macKeyCombination
            OsInfo.isLinux() -> linuxKeyCombination
            else -> winKeyCombination
        }
    )

    fun match(keyEvent: KeyEvent) = keyCombination.match(keyEvent)
}