package org.myteer.novel.gui.bookmanager

import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.geometry.Orientation
import javafx.scene.control.SplitPane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import org.myteer.novel.config.Preferences
import org.myteer.novel.db.data.Book
import org.myteer.novel.gui.bookmanager.BookManagerView.Companion.COL_CONFIG_KEY
import org.myteer.novel.gui.control.RecordFindControl
import org.myteer.novel.gui.utils.runOutsideUI
import org.slf4j.LoggerFactory

class BookManagerViewBase(
    private val preferences: Preferences,
    private val baseItems: ObservableList<Book>
) : SplitPane() {
    val table: BookTable = buildBookTable()

    private val findDialogVisibleProperty: BooleanProperty = object : SimpleBooleanProperty() {
        override fun invalidated() {
            when {
                get() -> showFindDialog()
                else -> {
                    hideFindDialog()
                    table.items = baseItems
                }
            }
        }
    }

    var isFindDialogVisible: Boolean
        get() = findDialogVisibleProperty.get()
        set(value) {
            findDialogVisibleProperty.set(value)
        }

    init {
        styleClass.add("book-manager-view")
        orientation = Orientation.VERTICAL
        buildUI()
    }

    private fun buildUI() {
        items.add(VBox(table))
    }

    private fun buildBookTable() = BookTable().apply {
        items = baseItems
        columns.addListener(ListChangeListener { updateTableColumnsConfiguration() })
        VBox.setVgrow(this, Priority.ALWAYS)
    }

    private fun showFindDialog() {
        logger.debug("Showing find dialog...")
        val recordFindControl = buildRecordFindControl()
        (table.parent as VBox).children.add(0, recordFindControl)
        recordFindControl.requestFocus()
    }

    private fun hideFindDialog() {
        logger.debug("Hiding find dialog...")
        val iterator = (table.parent as VBox).children.iterator()
        while (iterator.hasNext()) {
            val element = iterator.next()
            if (element is RecordFindControl) {
                element.releaseListeners()
                iterator.remove()
                break
            }
        }
    }

    private fun buildRecordFindControl() = RecordFindControl(baseItems).apply {
        onNewResults = fun(items) {
            table.items = FXCollections.observableArrayList(items)
            table.refresh()
        }
        onCloseRequest = fun() { isFindDialogVisible = false }
    }

    private fun updateTableColumnsConfiguration() {
        val columns = table.getShowingColumnTypes()
        runOutsideUI {
            preferences.editor().put(COL_CONFIG_KEY, BookManagerView.TableColumnsInfo(columns))
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(BookManagerViewBase::class.java)
    }
}