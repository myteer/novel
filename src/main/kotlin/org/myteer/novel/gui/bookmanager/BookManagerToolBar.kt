package org.myteer.novel.gui.bookmanager

import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ObservableBooleanValue
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.control.*
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.bookmanager.config.BookManagerViewConfigurationOverlay
import org.myteer.novel.gui.control.BiToolBar
import org.myteer.novel.gui.control.KeyBindingTooltip
import org.myteer.novel.gui.keybinding.KeyBinding
import org.myteer.novel.gui.keybinding.KeyBindings
import org.myteer.novel.gui.utils.emptyBinding
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.gui.utils.selectedItems
import org.myteer.novel.i18n.i18n

class BookManagerToolBar(
    private val context: Context,
    private val bookManagerView: BookManagerView
) : BiToolBar() {
    init {
        styleClass.add("book-manager-toolbar")
        buildUI()
    }

    private fun buildUI() {
        leftItems.addAll(
            buildRefreshItem(),
            Separator(),
            buildCountItem(),
            Separator(),
            buildExportItem(),
            Separator(),
            buildDeleteItem(),
            Separator(),
            buildSearchItem()
        )
        rightItems.add(buildOptionsItem())
    }

    private fun buildRefreshItem() = buildToolBarItem(
        "reload-icon",
        "page.reload",
        KeyBindings.refreshPage
    ) { bookManagerView.refresh() }

    private fun buildCountItem() = Label().apply {
        padding = Insets(5.0)
        textProperty().bind(
            SimpleStringProperty("")
                .concat(bookManagerView.selectedItemsCountProperty())
                .concat("/")
                .concat(bookManagerView.itemsCountProperty())
                .concat(" ")
                .concat(i18n("record.items_selected"))
        )
    }

    private fun buildExportItem() = MenuButton().apply {
        styleClass.add("options-item")
        graphic = icon("file-export-icon")
        tooltip = Tooltip(i18n("record.export"))
        disableProperty().bind(bookManagerView.table.selectedItems.emptyBinding())
    }

    private fun buildDeleteItem() = buildToolBarItem(
        "delete-icon",
        "record.delete",
        KeyBindings.deleteRecord,
        bookManagerView.table.selectedItems.emptyBinding()
    ) { bookManagerView.removeSelectedItems() }

    private fun buildSearchItem() = buildToolBarItem(
        "search-icon",
        "record.find",
        KeyBindings.findRecord
    ) { bookManagerView.isFindDialogVisible = bookManagerView.isFindDialogVisible.not() }

    private fun buildOptionsItem() = buildToolBarItem(
        "tune-icon",
        "record.panel_config"
    ) { context.showOverlay(BookManagerViewConfigurationOverlay(bookManagerView)) }

    private fun buildToolBarItem(
        iconStyleClass: String,
        i18nTooltip: String,
        keyBinding: KeyBinding? = null,
        disable: ObservableBooleanValue? = null,
        onClick: EventHandler<ActionEvent>
    ) = Button().apply {
        graphic = icon(iconStyleClass)
        tooltip = keyBinding?.let { KeyBindingTooltip(i18n(i18nTooltip), it) } ?: Tooltip(i18n(i18nTooltip))
        disable?.let(disableProperty()::bind)
        onAction = onClick
    }
}