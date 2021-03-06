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
package org.myteer.novel.gui.preferences

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.control.ScrollPane
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import org.myteer.novel.config.Preferences
import org.myteer.novel.gui.base.BaseView
import org.myteer.novel.gui.preferences.pane.*

class PreferencesView(private val preferences: Preferences) : BaseView() {
    private val tabPane: TabPane = TabPane()

    init {
        styleClass.add("preferences-view")
        setContent(tabPane)
        initPanes()
    }

    private fun initPanes() {
        listOf(
            AppearancePane(preferences),
            KeyBindingPane(preferences),
            LanguagePane(this, preferences),
            UpdatePane(preferences),
            AdvancedPane(this, preferences)
        ).forEach(this::addPane)
    }

    private fun addPane(pane: PreferencesPane) {
        tabPane.tabs.add(Tab(pane.title).apply {
            graphic = pane.graphic
            isClosable = false
            selectedProperty().addListener(object : ChangeListener<Boolean> {
                override fun changed(
                    observable: ObservableValue<out Boolean>,
                    oldValue: Boolean,
                    newValue: Boolean
                ) {
                    if (newValue) {
                        this@apply.content = ScrollPane(pane.getContent()).apply {
                            isFitToWidth = true
                            isFitToHeight = true
                        }
                        observable.removeListener(this)
                    }
                }
            })
        })
    }
}