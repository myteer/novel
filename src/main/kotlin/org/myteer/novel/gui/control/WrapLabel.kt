package org.myteer.novel.gui.control

import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import org.myteer.novel.gui.utils.icon

class WrapLabel(private val text: String, private val initHeight: Double) : VBox() {
    private val isWrapped: BooleanProperty = SimpleBooleanProperty(true)
    private val label: Label = buildLabel()
    private val icon: StackPane = buildIcon()

    init {
        styleClass.add("wrap-label")
        buildUI()
    }

    @Suppress("UNCHECKED_CAST")
    private fun buildUI() {
        children.add(label)
        children.add(icon)
        label.widthProperty().addListener(object : ChangeListener<Double> {
            override fun changed(observable: ObservableValue<out Double>?, oldValue: Double?, newValue: Double?) {
                newValue?.let { width ->
                    if (width > 0) {
                        if (label.prefHeight(width) < initHeight) {
                            icon.isVisible = false
                            icon.isManaged = false
                        } else {
                            isWrapped.set(false)
                        }
                        observable?.removeListener(this)
                    }
                }
            }
        } as ChangeListener<in Number>)
    }

    private fun buildLabel() = Label(text).apply {
        maxHeight = Double.MAX_VALUE
        isWrapText = true
    }

    private fun buildIcon() = StackPane().apply {
        isWrapped.addListener { _, _, wrapped ->
            when {
                wrapped -> {
                    label.maxHeight = Double.MAX_VALUE
                    label.isWrapText = wrapped
                    label.minHeight = label.prefHeight(label.width)
                    children.setAll(icon("direction-up-icon").apply {
                        setOnMouseClicked {
                            isWrapped.set(!wrapped)
                        }
                    })
                }
                else -> {
                    label.maxHeight = initHeight
                    label.isWrapText = wrapped
                    label.minHeight = USE_COMPUTED_SIZE
                    children.setAll(icon("direction-down-icon").apply {
                        setOnMouseClicked {
                            isWrapped.set(!wrapped)
                        }
                    })
                }
            }
        }
    }
}