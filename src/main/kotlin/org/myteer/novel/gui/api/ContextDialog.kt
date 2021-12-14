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
package org.myteer.novel.gui.api

import javafx.beans.property.BooleanProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.ReadOnlyBooleanProperty
import javafx.beans.property.StringProperty
import javafx.collections.ObservableList
import javafx.event.Event
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.control.ButtonType
import java.util.function.Consumer

interface ContextDialog {
    enum class Type {
        INPUT,
        INFORMATION,
        ERROR,
        WARNING,
        CONFIRMATION
    }

    fun getType(): Type?

    fun getButtonTypes(): ObservableList<ButtonType>

    fun getStyleClass(): ObservableList<String>

    fun onShownProperty(): ObjectProperty<EventHandler<Event>>

    fun setOnShown(handler: EventHandler<Event>)

    fun getOnShown(): EventHandler<Event>

    fun onHiddenProperty(): ObjectProperty<EventHandler<Event>>

    fun setOnHidden(handler: EventHandler<Event>)

    fun getOnHidden(): EventHandler<Event>

    fun maximizedProperty(): BooleanProperty

    fun setMaximized(max: Boolean)

    fun isMaximized(): Boolean

    fun contentProperty(): ObjectProperty<Node>

    fun setContent(content: Node)

    fun getContent(): Node

    fun titleProperty(): StringProperty

    fun setTitle(title: String)

    fun getTitle(): String

    fun exceptionProperty(): ObjectProperty<Exception>

    fun setException(exception: Exception)

    fun getException(): Exception

    fun detailsProperty(): StringProperty

    fun setDetails(details: String)

    fun getDetails(): String

    fun blockingProperty(): BooleanProperty

    fun setBlocking(blocking: Boolean)

    fun isBlocking(): Boolean

    fun onResultProperty(): ObjectProperty<Consumer<ButtonType>>

    fun setOnResult(onResult: Consumer<ButtonType>)

    fun getOnResult(): Consumer<ButtonType>

    fun showingProperty(): ReadOnlyBooleanProperty

    fun isShowing(): Boolean
}