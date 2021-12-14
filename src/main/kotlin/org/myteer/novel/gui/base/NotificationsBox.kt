/*
 * Copyright (c) 2021 MTSoftware
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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