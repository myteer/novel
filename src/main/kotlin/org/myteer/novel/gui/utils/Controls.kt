package org.myteer.novel.gui.utils

import com.dlsc.workbenchfx.Workbench
import com.dlsc.workbenchfx.model.WorkbenchOverlay
import javafx.beans.property.DoubleProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.collections.ObservableList
import javafx.collections.ObservableMap
import javafx.geometry.Orientation
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.util.StringConverter
import org.controlsfx.control.CheckListView

fun <T : Node> T.styleClass(styleClass: String) = apply {
    getStyleClass().add(styleClass)
}

fun <T : Node> T.colspan(value: Int) = apply {
    GridPane.setColumnSpan(this, value)
}

fun <T : Node> T.hgrow(priority: Priority) = apply {
    GridPane.setHgrow(this, priority)
    HBox.setHgrow(this, priority)
}

fun ButtonType.typeEquals(other: ButtonType) = (buttonData == other.buttonData)

fun <T> ComboBox<T>.refresh() {
    val items = this.items
    val selected = this.selectionModel.selectedItem
    this.items = null
    this.items = items
    this.selectionModel.select(selected)
}

inline val <S> TableView<S>.selectedItems: ObservableList<S>
    get() = selectionModel.selectedItems

inline val <T> ListView<T>.selectedItems: ObservableList<T>
    get() = selectionModel.selectedItems

inline val <T> CheckListView<T>.checkedItems: ObservableList<T>
    get() = checkModel.checkedItems

inline var <T> ChoiceBox<T>.selectedItem: T
    get() = selectionModel.selectedItem
    set(value) {
        selectionModel.select(value)
    }

inline var <T> ComboBox<T>.selectedItem: T
    get() = selectionModel.selectedItem
    set(value) {
        selectionModel.select(value)
    }

inline fun <T> ChoiceBox<T>.valueConvertingPolicy(
    crossinline toStringFun: (T) -> String?,
    crossinline fromStringFun: (String?) -> T
) {
    converter = object : StringConverter<T>() {
        override fun toString(obj: T) = toStringFun(obj)
        override fun fromString(string: String) = fromStringFun(string)
    }
}

fun <T> ChoiceBox<T>.selectedItemProperty(): ReadOnlyObjectProperty<T> = selectionModel.selectedItemProperty()

inline fun hyperlink(text: String, graphic: Node? = null, crossinline onAction: () -> Unit) =
    Hyperlink(text, graphic).apply {
        setOnAction { onAction() }
    }

fun scrollPane(content: Node, fitToWidth: Boolean = false, fitToHeight: Boolean = false) = ScrollPane(content).apply {
    isFitToWidth = fitToWidth
    isFitToHeight = fitToHeight
}

fun GridPane.addRow(vararg elements: Node) = apply {
    addRow(rowCount, *elements)
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

val Workbench.mutableOverlays: ObservableMap<Region, WorkbenchOverlay>
    get() {
        @Suppress("UNCHECKED_CAST")
        return workbenchOverlaysField.get(this) as ObservableMap<Region, WorkbenchOverlay>
    }

private val workbenchOverlaysField = Workbench::class.java.getDeclaredField("overlays").apply {
    isAccessible = true
}
