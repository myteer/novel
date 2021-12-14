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
package org.myteer.novel.gui.main

import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import org.myteer.novel.config.Preferences
import org.myteer.novel.gui.control.tabview.TabItem
import org.myteer.novel.gui.control.tabview.TabView
import org.myteer.novel.gui.entry.DatabaseTracker

class MainViewBase(
    view: MainView,
    preferences: Preferences,
    databaseTracker: DatabaseTracker
) : VBox() {
    private val toolBar = MainViewToolBar(view, preferences, databaseTracker)
    private val tabView: TabView = TabView(ModuleView.getTabItem(view))

    init {
        buildUI()
    }

    private fun buildUI() {
        setVgrow(tabView, Priority.ALWAYS)
        children.add(toolBar)
        children.add(tabView)
    }

    fun openTab(tabItem: TabItem) = tabView.openTab(tabItem)

    fun closeTab(tabItem: TabItem) = tabView.closeTab(tabItem)
}