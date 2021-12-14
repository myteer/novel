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
package org.myteer.novel.gui.control.tabview

import javafx.scene.Node

open class TabItem(
    val id: String,
    val title: String,
    private val graphicFactory: (() -> Node?)? = null,
    private val contentFactory: (() -> Node)? = null,
    private val onCloseRequest: ((Node) -> Boolean)? = null
) {
    open val graphic: Node?
        get() = graphicFactory?.invoke()

    open val content: Node
        get() = requireNotNull(contentFactory?.invoke()) { "Content of TabItem should not be null" }

    open fun onClose(content: Node): Boolean {
        return onCloseRequest?.invoke(content) ?: true
    }
}