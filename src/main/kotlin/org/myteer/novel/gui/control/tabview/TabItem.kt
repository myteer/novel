package org.myteer.novel.gui.control.tabview

import javafx.scene.Node

open class TabItem(
    val id: String,
    val title: String,
    private val graphicFactory: (() -> Node?)? = null,
    private val contentFactory: (() -> Node)? = null,
    private val onCloseRequest: ((Node) -> Boolean)? = null
) {
    open val graphic: Node?
        get() = graphicFactory?.invoke()

    open val content: Node
        get() = requireNotNull(contentFactory?.invoke()) { "Content of TabItem should not be null" }

    open fun onClose(content: Node): Boolean {
        return onCloseRequest?.invoke(content) ?: true
    }
}