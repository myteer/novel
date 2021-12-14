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
package org.myteer.novel.gui.preferences.pane

import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.Label
import org.myteer.novel.config.PreferenceKey
import org.myteer.novel.config.Preferences
import org.myteer.novel.gui.utils.asCentered
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.gui.utils.onValuePresent
import org.myteer.novel.i18n.i18n
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UpdatePane(preferences: Preferences) : PreferencesPane(preferences) {
    override val title: String = i18n("preferences.tab.update")
    override val graphic: Node = icon("update-icon")

    override fun buildContent(): Content = object : Content() {
        init {
            items.add(buildAutoSearchToggle())
            items.add(buildLastTimeSearchLabel())
        }

        private fun buildAutoSearchToggle(): PreferencesControl {
            return ToggleControl(
                i18n("preferences.update.automatic"),
                i18n("preferences.update.automatic.desc")
            ).apply {
                isSelected = preferences.get(PreferenceKey.SEARCH_UPDATES)
                selectedProperty().onValuePresent {
                    preferences.editor().put(PreferenceKey.SEARCH_UPDATES, it)
                }
            }
        }

        private fun buildLastTimeSearchLabel(): PreferencesControl {
            return PairControl(
                i18n("preferences.update.last"),
                customControl = Label(
                    preferences.get(PreferenceKey.LAST_UPDATE_SEARCH)
                        .takeIf { LocalDateTime.MIN != it }
                        ?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                ).asCentered(Pos.CENTER_RIGHT)
            )
        }
    }
}