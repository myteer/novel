package org.myteer.novel.gui.utils

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyEvent

fun KeyEvent.isUndefined(): Boolean = code.name.equals("undefined", true)

fun KeyEvent.asKeyCombination(): KeyCombination? {
    return mutableListOf<KeyCombination.Modifier>().also { modifiers ->
        isControlDown.takeIf { it }?.let { modifiers.add(KeyCombination.CONTROL_DOWN) }
        isAltDown.takeIf { it }?.let { modifiers.add(KeyCombination.ALT_DOWN) }
        isShiftDown.takeIf { it }?.let { modifiers.add(KeyCombination.SHIFT_DOWN) }
        isMetaDown.takeIf { it }?.let { modifiers.add(KeyCombination.META_DOWN) }
        isShortcutDown.takeIf { it }?.let { modifiers.add(KeyCombination.SHORTCUT_DOWN) }
    }.let { modifiers ->
        code?.let {
            try {
                when {
                    modifiers.isEmpty()
                        .and(!code.isFunctionKey)
                        .and(!code.isNavigationKey)
                        .and(KeyCode.DELETE != code)
                        .and(KeyCode.INSERT != code) -> null
                    else -> KeyCodeCombination(code, *modifiers.toTypedArray())
                }
            } catch (e: RuntimeException) {
                null
            }
        }
    }
}