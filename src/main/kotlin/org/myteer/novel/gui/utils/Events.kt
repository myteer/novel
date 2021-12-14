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

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.Node
import javafx.scene.Scene
import javafx.stage.Window
import java.util.function.Consumer

fun Node.onWindowPresent(action: Consumer<Window>) {
    onWindowPresent {
        action.accept(it)
    }
}

inline fun Node.onWindowPresent(crossinline action: (Window) -> Unit) {
    scene?.window?.let { action(it) } ?: onScenePresent {
        it.windowProperty().addListener(object : ChangeListener<Window> {
            override fun changed(observable: ObservableValue<out Window>?, oldValue: Window?, newValue: Window?) {
                newValue?.let {
                    action(newValue)
                    observable?.removeListener(this)
                }
            }
        })
    }
}

inline fun Node.onScenePresent(crossinline action: (Scene) -> Unit) {
    scene?.let { action(it) } ?: sceneProperty().addListener(object : ChangeListener<Scene> {
        override fun changed(observable: ObservableValue<out Scene>?, oldValue: Scene?, newValue: Scene?) {
           newValue?.let {
               action(it)
               observable?.removeListener(this)
           }
        }
    })
}