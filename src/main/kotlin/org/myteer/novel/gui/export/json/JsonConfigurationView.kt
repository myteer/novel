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
package org.myteer.novel.gui.export.json

import javafx.collections.ListChangeListener
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.Priority
import jfxtras.styles.jmetro.JMetroStyleClass
import org.myteer.novel.export.json.JsonExportConfiguration
import org.myteer.novel.gui.control.CheckListView
import org.myteer.novel.gui.export.control.BaseConfigurationView
import org.myteer.novel.gui.utils.addRow
import org.myteer.novel.gui.utils.checkedItems
import org.myteer.novel.i18n.i18n

class JsonConfigurationView(
    private val onFinished: (JsonExportConfiguration) -> Unit
) : BaseConfigurationView<JsonExportConfiguration>(JsonExportConfiguration()) {
    init {
        styleClass.add(JMetroStyleClass.BACKGROUND)
        styleClass.add("json-configuration-view")
        buildUI()
    }

    private fun buildUI() {
        addRow(Label(i18n("book.export.json.options")))
        addRow(JsonOptionsChecker(exportConfiguration))
        addRow(buildExecuteButton())
    }

    private fun buildExecuteButton() = Button().apply {
        setColumnSpan(this, 3)
        maxWidth = Double.MAX_VALUE
        text = i18n("book.export.execute")
        isDefaultButton = true
        setOnAction {
            onFinished(exportConfiguration)
        }
    }

    private class JsonOptionsChecker(
        jsonExportConfiguration: JsonExportConfiguration
    ) : CheckListView<JsonOptionsChecker.Entry>() {
        init {
            setVgrow(this, Priority.SOMETIMES)
            setColumnSpan(this, 3)
            items.addAll(
                Entry(
                    i18n("book.export.json.pretty_printing"),
                    jsonExportConfiguration.prettyPrinting
                ) { jsonExportConfiguration.prettyPrinting = it },
                Entry(
                    i18n("book.export.json.non_executable"),
                    jsonExportConfiguration.nonExecutableJson
                ) { jsonExportConfiguration.nonExecutableJson = it },
                Entry(
                    i18n("book.export.json.serialize_nulls"),
                    jsonExportConfiguration.serializeNulls
                ) { jsonExportConfiguration.serializeNulls = it }
            )
            items.filter { it.checkedInitially }.forEach(checkModel::check)
            checkedItems.addListener(ListChangeListener {
                val uncheckedItems = items - checkedItems
                checkedItems.forEach { it(true) }
                uncheckedItems.forEach { it(false) }
            })
        }

        private class Entry(
            private val text: String,
            val checkedInitially: Boolean,
            action: (Boolean) -> Unit
        ) : (Boolean) -> Unit by action {
            override fun toString() = text
        }
    }
}