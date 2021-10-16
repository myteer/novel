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