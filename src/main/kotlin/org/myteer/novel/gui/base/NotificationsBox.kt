package org.myteer.novel.gui.base

import animatefx.animation.FadeIn
import animatefx.animation.FadeOut
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.util.Duration

class NotificationsBox : Group() {
    private val vBox: VBox

    init {
        StackPane.setAlignment(this, Pos.BOTTOM_RIGHT)
        StackPane.setMargin(this, Insets(0.0, 20.0, 0.0, 0.0))
        children.add(VBox().apply {
            vBox = this
            alignment = Pos.CENTER_RIGHT
        })
    }

    fun pushItem(notificationNode: NotificationNode, duration: Duration?) {
        vBox.children.add(notificationNode)
        FadeIn(notificationNode).apply {
            duration?.let {
                setOnFinished {
                    FadeOut(notificationNode).apply {
                        setDelay(duration)
                        setOnFinished {
                            vBox.children.remove(notificationNode)
                        }
                    }.play()
                }
            }
        }.play()
    }

    fun removeItem(notificationNode: NotificationNode) {
        FadeOut(notificationNode).apply {
            setOnFinished {
                vBox.children.remove(notificationNode)
            }
        }.play()
    }
}