package org.myteer.novel.gui.utils

import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyEvent
import javafx.scene.layout.Region
import org.myteer.novel.gui.keybinding.KeyBinding
import java.lang.ref.WeakReference

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

fun Region.addKeyBindingDetection(keyBinding: KeyBinding, action: (KeyBinding) -> Unit) {
    onScenePresent {
        it.addKeyBindingDetection(this, keyBinding, action)
    }
}

private fun Scene.addKeyBindingDetection(node: Node, keyBinding: KeyBinding, action: (KeyBinding) -> Unit) {
    node.properties["keyBindDetectionActionCache"]?.run {
        @Suppress("UNCHECKED_CAST")
        takeIf { it is MutableList<*> }?.let { it as MutableList<Any> }?.add(action) ?: RuntimeException()
    } ?: run {
        node.properties["keyBindDetectionActionCache"] = mutableListOf(action)
    }

    val nodeWeakReference = WeakReference(node)
    val actionWeakReference = WeakReference(action)

    var eventHandler: EventHandler<KeyEvent>? = null
    eventHandler = EventHandler {
        if (nodeWeakReference.get()?.scene == this) {
            if (keyBinding.match(it)) {
                actionWeakReference.get()?.invoke(keyBinding)
            }
        } else {
            removeEventHandler(KeyEvent.KEY_PRESSED, eventHandler)
        }
    }
    addEventHandler(KeyEvent.KEY_PRESSED, eventHandler)
}