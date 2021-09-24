package org.myteer.novel.gui.utils

import javafx.scene.Node
import javafx.scene.control.ButtonType
import javafx.scene.control.ComboBox

fun <T : Node> T.styleClass(styleClass: String) = apply {
    getStyleClass().add(styleClass)
}

fun ButtonType.typeEquals(other: ButtonType) = (buttonData == other.buttonData)

fun <T> ComboBox<T>.refresh() {
    val items = this.items
    val selected = this.selectionModel.selectedItem
    this.items = null
    this.items = items
    this.selectionModel.select(selected)
}
