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