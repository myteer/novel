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
package org.myteer.novel.gui.wizard.segment.lang

import javafx.geometry.Insets
import javafx.scene.control.ListView
import javafx.scene.layout.StackPane
import org.myteer.novel.config.PreferenceKey
import org.myteer.novel.config.Preferences
import org.myteer.novel.i18n.I18N
import java.util.*

class LanguageSegmentView(private val preferences: Preferences) : StackPane() {
    init {
        padding = Insets(10.0)
        buildUI()
    }

    private fun buildUI() {
        children.add(buildListView())
    }

    private fun buildListView() = ListView<LanguageEntry>().apply {
        selectionModel.selectedItemProperty().addListener { _, ov, nv ->
            nv?.let {
                preferences.editor().put(PreferenceKey.LOCALE, it.locale)
                Locale.setDefault(it.locale)
            } ?: selectionModel.select(ov)
        }
        fillListView()
    }

    private fun ListView<LanguageEntry>.fillListView() {
        val availableLocales = I18N.getAvailableLocales().toList()
        val defaultLocaleIndex = availableLocales.indexOf(I18N.defaultLocale())
        items.addAll(availableLocales.map(::LanguageEntry))
        selectionModel.select(defaultLocaleIndex)
        scrollTo(defaultLocaleIndex)
    }

    private class LanguageEntry(val locale: Locale) {
        override fun toString(): String {
            return locale.displayName
        }
    }
}