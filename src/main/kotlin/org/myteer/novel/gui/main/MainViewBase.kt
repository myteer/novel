package org.myteer.novel.gui.main

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
        children.add(toolBar)
        children.add(tabView)
    }

    fun openTab(tabItem: TabItem) = tabView.openTab(tabItem)

    fun closeTab(tabItem: TabItem) = tabView.closeTab(tabItem)
}