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