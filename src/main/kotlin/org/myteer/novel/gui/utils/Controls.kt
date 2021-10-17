package org.myteer.novel.gui.utils

import javafx.beans.property.DoubleProperty
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

val <S> TableView<S>.verticalScrollValueProperty: DoubleProperty?
    get() = lookupAll(".scroll-bar")
        .filterIsInstance<ScrollBar>()
        .find { Orientation.VERTICAL == it.orientation }
        ?.valueProperty()

fun <S> TableView<S>.setOnScrolledToBottom(action: () -> Unit): TableView<S> = apply {
    onScenePresent {
        runOutsideUIAsync {
            var verticalScrollBar: ScrollBar? = null
            while (null == verticalScrollBar) {
                verticalScrollBar = lookupAll(".scroll-bar")
                    .filterIsInstance<ScrollBar>()
                    .find { Orientation.VERTICAL == it.orientation }
                if (null == verticalScrollBar) {
                    Thread.sleep(100)
                }
            }
            verticalScrollBar.let {
                it.valueProperty().addListener { _, _, value ->
                    if (value == it.max) {
                        action.invoke()
                    }
                }
                it.visibleProperty().addListener { _, _, visible ->
                    if (!visible) {
                        action.invoke()
                    }
                }
            }
        }
    }
}
