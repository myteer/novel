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
package org.myteer.novel.gui.export.control

import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import org.myteer.novel.db.data.BookProperty
import org.myteer.novel.export.api.BookExportConfiguration
import org.myteer.novel.gui.control.CheckListView
import org.myteer.novel.gui.utils.checkedItems

class BookPropertyChecker(exportConfiguration: BookExportConfiguration) : CheckListView<BookProperty<*>>() {
    init {
        GridPane.setColumnSpan(this, 3)
        GridPane.setVgrow(this, Priority.SOMETIMES)
        items = FXCollections.observableArrayList(exportConfiguration.availableFields)
        exportConfiguration.requiredFields.forEach(checkModel::check)
        checkedItems.addListener(ListChangeListener {
            exportConfiguration.requiredFields = checkedItems.toList()
        })
    }
}