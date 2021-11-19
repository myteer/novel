package org.myteer.novel.gui.base

import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.Separator
import javafx.scene.layout.*
import org.myteer.novel.gui.utils.icon

open class TitledOverlayBox(
    titleProperty: StringProperty,
    graphic: Node,
    content: Node,
    resizableH: Boolean = true,
    resizableV: Boolean = true,
    vararg customTitleBarItems: Node
) : StackPane(Group(ResizablePane(InnerVBox(titleProperty, graphic, content, resizableH, resizableV, customTitleBarItems)))) {
    constructor(
        title: String,
        graphic: Node,
        content: Node,
        resizableH: Boolean = true,
        resizableV: Boolean = true,
        vararg customTitleBarItems: Node
    ) : this(SimpleStringProperty(title), graphic, content, resizableH, resizableV, *customTitleBarItems)

    init {
        isPickOnBounds = false
    }

    private class ResizablePane(val content: InnerVBox) : BorderPane(content) {
        companion object {
            private const val PREFERRED_RESIZE_AREA_SIZE = 3.5
            private const val PREFERRED_RESIZE_SPEED = 2.0
        }

        init {
            if (content.resizableV) {
                top = buildTop()
                bottom = buildBottom()
            }
            if (content.resizableH) {
                left = buildLeft()
                right = buildRight()
            }
        }

        private fun buildTop() = StackPane().apply {
            cursor = Cursor.V_RESIZE
            prefHeight = PREFERRED_RESIZE_AREA_SIZE
            var released = false
            var lastY: Double? = null
            setOnMouseReleased { released = true }
            setOnMouseDragged {
                if (null === lastY || released) {
                    lastY = it.sceneY
                    released = false
                }
                content.prefHeight = content.height - (it.sceneY - lastY!!) * PREFERRED_RESIZE_SPEED
                lastY = it.sceneY
            }
        }

        private fun buildBottom() = StackPane().apply {
            cursor = Cursor.V_RESIZE
            prefHeight = PREFERRED_RESIZE_AREA_SIZE
            var released = false
            var lastY: Double? = null
            setOnMouseReleased { released = true }
            setOnMouseDragged {
                if (null === lastY || released) {
                    lastY = it.sceneY
                    released = false
                }
                content.prefHeight = content.height + (it.sceneY - lastY!!) * PREFERRED_RESIZE_SPEED
                lastY = it.sceneY
            }
        }

        private fun buildLeft() = StackPane().apply {
            cursor = Cursor.H_RESIZE
            prefWidth = PREFERRED_RESIZE_AREA_SIZE
            var released = false
            var lastX: Double? = null
            setOnMouseReleased { released = true }
            setOnMouseDragged {
                if (null === lastX || released) {
                    lastX = it.sceneX
                    released = false
                }
                content.prefWidth = content.width - (it.sceneX - lastX!!) * PREFERRED_RESIZE_SPEED
                lastX = it.sceneX
            }
        }

        private fun buildRight() = StackPane().apply {
            cursor = Cursor.H_RESIZE
            prefWidth = PREFERRED_RESIZE_AREA_SIZE
            var released = false
            var lastX: Double? = null
            setOnMouseReleased { released = true }
            setOnMouseDragged {
                if (null === lastX || released) {
                    lastX = it.sceneX
                    released = false
                }
                content.prefWidth = content.width + (it.sceneX - lastX!!) * PREFERRED_RESIZE_SPEED
                lastX = it.sceneX
            }
        }
    }

    private class InnerVBox(
        titleProperty: StringProperty,
        graphic: Node,
        content: Node,
        val resizableH: Boolean,
        val resizableV: Boolean,
        customTitleBarItems: Array<out Node>
    ) : VBox() {
        init {
            styleClass.add("overlay-box")
            children.add(TitleBar(titleProperty, graphic, this, resizableH, resizableV, customTitleBarItems))
            children.add(content.apply {
                styleClass.add("content")
                setVgrow(this, Priority.ALWAYS)
            })
        }
    }

    private class TitleBar(
        titleProperty: StringProperty,
        graphic: Node,
        content: VBox,
        resizableH: Boolean,
        resizableV: Boolean,
        customTitleBarItems: Array<out Node>
    ) : BorderPane() {
        companion object {
            private const val RESIZE_UNIT = 35.0
        }

        init {
            styleClass.add("title-bar")
            left = graphic.apply {
                setAlignment(this, Pos.CENTER)
                setMargin(this, Insets(0.0, 0.0, 0.0, 5.0))
            }
            center = buildTitleLabel(titleProperty)
            right = buildRightBox(content, resizableH, resizableV, customTitleBarItems)
        }

        private fun buildTitleLabel(titleProperty: StringProperty) = Label().apply {
            textProperty().bind(titleProperty)
            setAlignment(this, Pos.CENTER_LEFT)
            setMargin(this, Insets(0.0, 0.0, 0.0, 5.0))
        }

        private fun buildRightBox(content: VBox, resizableH: Boolean, resizableV: Boolean, customTitleBarItems: Array<out Node>) = HBox(5.0).apply {
            setAlignment(this, Pos.CENTER_RIGHT)
            if (customTitleBarItems.isNotEmpty()) {
                customTitleBarItems.forEach(children::add)
                children.add(Separator(Orientation.VERTICAL))
            }
            if (resizableV) {
                children.add(buildButton("arrow-up-icon"){
                    content.prefHeight = content.height + RESIZE_UNIT
                })
                children.add(buildButton("arrow-down-icon"){
                    content.prefHeight = content.height - RESIZE_UNIT
                })
            }
            if (resizableH) {
                children.add(buildButton("arrow-left-icon"){
                    content.prefWidth = content.width - RESIZE_UNIT
                })
                children.add(buildButton("arrow-right-icon"){
                    content.prefWidth = content.width + RESIZE_UNIT
                })
            }
        }

        private fun buildButton(iconStyleClass: String, action: EventHandler<ActionEvent>) = Button().apply {
            graphic = icon(iconStyleClass)
            onAction = action
            padding = Insets(0.0)
        }
    }
}