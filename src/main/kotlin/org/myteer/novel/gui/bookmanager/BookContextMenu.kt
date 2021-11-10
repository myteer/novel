package org.myteer.novel.gui.bookmanager

import javafx.collections.ListChangeListener
import javafx.scene.control.ContextMenu
import javafx.scene.control.Menu
import javafx.scene.control.MenuItem
import javafx.scene.control.SeparatorMenuItem
import org.myteer.novel.export.SupportedExporters
import org.myteer.novel.gui.keybinding.KeyBindings
import org.myteer.novel.gui.utils.*
import org.myteer.novel.i18n.i18n

class BookContextMenu(private val bookManagerView: BookManagerView) : ContextMenu() {
    private val itemsEmpty = bookManagerView.table.selectedItems.emptyBinding()

    init {
        buildItems()
    }

    private fun buildItems() {
        items.addAll(
            buildDeleteItem(),
            buildExportItem(),
            SeparatorMenuItem(),
            buildReloadItem()
        )
    }

    private fun buildDeleteItem() = MenuItem().apply {
        text = i18n("record.delete")
        graphic = icon("delete-icon")
        disableProperty().bind(itemsEmpty)
        keyBinding(KeyBindings.deleteRecord)
        setOnAction {
            bookManagerView.removeSelectedItems()
        }
    }

    private fun buildExportItem() = Menu().apply {
        text = i18n("record.context_menu.export")
        graphic = icon("file-export-icon")
        disableProperty().bind(itemsEmpty)
        items.addAll(SupportedExporters.map {
            MenuItem(it.name, it.icon).action { _ ->
                bookManagerView.exportSelected(it)
            }
        })
    }

    private fun buildReloadItem() = MenuItem().apply {
        text = i18n("page.reload")
        graphic = icon("reload-icon")
        keyBinding(KeyBindings.refreshPage)
        setOnAction {
            bookManagerView.refresh()
        }
    }

    fun applyOn(table: BookTable) {
        table.setRowContextMenu(this)
        table.contextMenu = this.takeIf { table.items.isEmpty() }
        table.items.addListener(ListChangeListener {
            table.contextMenu = this.takeIf { table.items.isEmpty() }
        })
    }
}