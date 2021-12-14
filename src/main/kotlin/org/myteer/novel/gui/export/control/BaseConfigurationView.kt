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

import javafx.geometry.Insets
import javafx.scene.control.Label
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import org.myteer.novel.export.api.BookExportConfiguration
import org.myteer.novel.gui.utils.addRow
import org.myteer.novel.i18n.i18n

open class BaseConfigurationView<C : BookExportConfiguration>(
    protected val exportConfiguration: C
) : GridPane() {
    init {
        padding = Insets(20.0)
        hgap = 10.0
        vgap = 10.0
        buildUI()
    }

    private fun buildUI() {
        initColumnConstraints()
        addRow(
            Label(i18n("book.export.sort_by")),
            Label(i18n("book.export.sort_locale"))
        )
        addRow(
            SortingPropertyChooser(exportConfiguration),
            SortingLocaleChooser(exportConfiguration),
            ReverseItemsToggle(exportConfiguration)
        )
        addRow(Label(i18n("book.export.fields")))
        addRow(BookPropertyChecker(exportConfiguration))
    }

    private fun initColumnConstraints() {
        columnConstraints.addAll(
            List(3) {
                ColumnConstraints().apply {
                    hgrow = Priority.ALWAYS
                }
            }
        )
    }
}