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
package org.myteer.novel.gui.control

import javafx.beans.binding.Bindings
import javafx.beans.binding.BooleanBinding
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.TableView
import javafx.scene.layout.StackPane
import java.util.function.Supplier

open class TableViewPlaceHolder(
    tableView: TableView<*>,
    private val valueIfEmpty: Supplier<Node>? = null,
    private val valueIfNoColumns: Supplier<Node>? = null
) : StackPane() {
    private val noColumns: BooleanBinding = Bindings.isEmpty(tableView.columns)

    constructor(tableView: TableView<*>, valueIfEmpty: () -> String, valueIfNoColumns: () -> String) : this(
        tableView,
        Supplier { Label(valueIfEmpty()) },
        Supplier { Label(valueIfNoColumns()) }
    )

    init {
        buildUI()
    }

    private fun buildUI() {
        noColumns.addListener { _, _, newValue ->
            when {
                newValue -> children.setAll(Group(contentIfNoColumns()))
                else -> children.setAll(Group(contentIfEmpty()))
            }
        }
    }

    protected open fun contentIfEmpty(): Node? = valueIfEmpty?.get()

    protected open fun contentIfNoColumns(): Node? = valueIfNoColumns?.get()
}