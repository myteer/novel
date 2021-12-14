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
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.db.data.Book
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.bookmanager.BookManagerView.Companion.COL_CONFIG_KEY
import org.myteer.novel.gui.control.RecordFindControl
import org.myteer.novel.gui.main.MainView
import org.myteer.novel.gui.main.getTabItem
import org.myteer.novel.gui.utils.onScenePresent
import org.myteer.novel.gui.volume.VolumeTab
import org.slf4j.LoggerFactory

class BookManagerViewBase(
    private val context: Context,
    private val preferences: Preferences,
    private val database: NitriteDatabase,
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
        onScenePresent {
            columns.addListener(ListChangeListener { updateTableColumnsConfiguration() })
        }
        setOnItemDoubleClicked {
            context.sendRequest(
                MainView.TabItemOpenRequest(
                    VolumeTab(context, preferences, database, it).getTabItem()
                )
            )
        }
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
        preferences.editor().put(COL_CONFIG_KEY, BookManagerView.TableColumnsInfo(columns))
    }

    companion object {
        private val logger = LoggerFactory.getLogger(BookManagerViewBase::class.java)
    }
}