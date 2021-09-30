package org.myteer.novel.gui.main

import javafx.scene.Node

abstract class Module {
    abstract val id: String
    abstract val name: String
    abstract val icon: Node

    var isOpened: Boolean = false
        private set

    protected abstract fun buildContent(): Node

    protected abstract fun destroy(): Boolean

    fun activate(): Node = buildContent().also {
        isOpened = true
    }

    fun close(): Boolean = destroy().also {
        isOpened = !it
    }

    open fun sendMessage(message: Message) { }

    interface Message
}