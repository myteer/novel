package org.myteer.novel.gui.utils

import javafx.geometry.Orientation
import javafx.scene.Node
import javafx.scene.control.ButtonType
import javafx.scene.control.ComboBox
import javafx.scene.control.ScrollBar
import javafx.scene.control.TableView

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

fun <S> TableView<S>.setOnScrolledToBottom(action: () -> Unit): TableView<S> = also {
    lookupAll(".scroll-bar")
        .filterIsInstance<ScrollBar>()
        .find { Orientation.VERTICAL == it.orientation }
        ?.let {
            it.valueProperty().addListener { _, _, value ->
                println("scrolled to $value")
                if (value == it.max) {
                    println("scrolled to bottom")
                    action.invoke()
                }
            }
        }
}
