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

import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.control.Hyperlink
import javafx.scene.control.Label
import javafx.scene.input.MouseButton
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import org.myteer.novel.gui.utils.icon
import java.util.function.Consumer

class NotificationNode(
    type: NotificationType,
    title: String,
    message: String?,
    hyperlinks: Array<out Hyperlink>? = null,
    closeAction: Consumer<NotificationNode>
) : Group(EntryPanel(type, title, message, hyperlinks, closeAction)) {

    enum class NotificationType(val iconStyleClass: String) {
        INFO("info-icon"),
        WARNING("warning-circle-icon"),
        ERROR("close-circle-icon")
    }

    private class EntryPanel(
        type: NotificationType,
        title: String,
        message: String?,
        hyperlinks: Array<out Hyperlink>?,
        private val closeAction: Consumer<NotificationNode>
    ) : HBox(8.0) {
        init {
            styleClass.add("notification-node")
            children.add(icon(type.iconStyleClass))
            children.add(ContentPanel(title, message, hyperlinks))
            children.add(buildCloseButton())
        }

        private fun buildCloseButton(): Node = icon("close-icon").apply {
            isVisible = false
            this@EntryPanel.setOnMouseEntered { isVisible = true }
            this@EntryPanel.setOnMouseExited { isVisible = false }
            setOnMouseClicked {
                if (it.button == MouseButton.PRIMARY) {
                    closeAction.accept(this@EntryPanel.parent as NotificationNode)
                }
            }
        }
    }

    private class ContentPanel(title: String, message: String?, hyperlinks: Array<out Hyperlink>?) : StackPane() {
        init {
            children.add(Group(VBox(2.0).apply {
                styleClass.add("content-panel")
                children.add(buildTitleLabel(title))
                message.takeIf { it?.isNotBlank() ?: false }?.let { children.add(buildMessageLabel(it)) }
                hyperlinks.takeIf { it?.isNotEmpty() ?: false }?.let { children.add(HBox(2.0, *it)) }
            }))
        }

        private fun buildTitleLabel(text: String) = Label(text).apply { styleClass.add("title") }

        private fun buildMessageLabel(text: String) = Label(text).apply { styleClass.add("message") }
    }
}