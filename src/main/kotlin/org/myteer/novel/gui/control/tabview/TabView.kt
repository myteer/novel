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
package org.myteer.novel.gui.control.tabview

import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleIntegerProperty
import javafx.collections.ListChangeListener
import javafx.event.Event
import javafx.event.EventHandler
import javafx.scene.control.*
import javafx.scene.layout.StackPane
import org.myteer.novel.i18n.i18n
import org.slf4j.LoggerFactory

class TabView(
    private val baseTabItem: TabItem,
    private val showBaseTabItemWhenAllClosed: Boolean = true
) : StackPane() {
    private val idsWithItems: MutableMap<String, TabItem> = HashMap()
    private val tabPane: TabPane = buildTabPane()

    init {
        styleClass.add("tab-view")
        buildUI()
        initTab()
    }

    private fun buildTabPane() = TabPane().apply {
        tabClosingPolicy = TabPane.TabClosingPolicy.ALL_TABS
        tabDragPolicy = TabPane.TabDragPolicy.REORDER
    }

    private fun buildUI() {
        children.add(tabPane)
    }

    private fun initTab() {
        openTab(baseTabItem)
        if (showBaseTabItemWhenAllClosed) {
            tabPane.tabs.addListener(ListChangeListener {
                if (tabPane.tabs.isEmpty()) {
                    openTab(baseTabItem)
                }
            })
        }
    }

    fun openTab(item: TabItem) {
        if (idsWithItems.containsKey(item.id)) {
            logger.debug("Tab with id '{}' found", item.id)
            selectTab(idsWithItems[item.id]!!)
        } else {
            logger.debug("Tab with id '{}' not found", item.id)
            idsWithItems[item.id] = item
            makeTab(item) {
                idsWithItems.remove(item.id)
                logger.debug("Tab with id '{}' removed", item.id)
            }
        }
    }

    private fun selectTab(item: TabItem) {
        item.tabImpl?.let(tabPane.selectionModel::select)
    }

    private fun makeTab(item: TabItem, onClose: EventHandler<Event>) {
        val tab = TabImpl(this, item).apply {
            setOnCloseRequest {
                if (item.onClose(content)) {
                    onClose.handle(it)
                } else {
                    it.consume()
                }
            }
        }
        tabPane.tabs.add(tab)
        tabPane.selectionModel.select(tab)
    }

    fun closeTab(item: TabItem) {
        item.tabImpl?.let(::closeTabImpl)
    }

    private fun closeTabImpl(tab: TabImpl) {
        val event = Event(Tab.TAB_CLOSE_REQUEST_EVENT)
        tab.onCloseRequest.handle(event)
        if (!event.isConsumed) {
            tabPane.tabs.remove(tab)
        }
    }

    private val TabItem.tabImpl: TabImpl?
        get() = tabPane.tabs.filterIsInstance<TabImpl>().find { it.tabItem == this }

    private class TabImpl(private val tabView: TabView, val tabItem: TabItem) : Tab() {
        private val indexProperty = SimpleIntegerProperty().also { property ->
            val tabs = tabView.tabPane.tabs
            tabs.addListener(object : ListChangeListener<Tab> {
                override fun onChanged(c: ListChangeListener.Change<out Tab>?) {
                    if (tabs.contains(this@TabImpl)) {
                        property.set(tabs.indexOf(this@TabImpl))
                    } else {
                        tabs.removeListener(this)
                    }
                }
            })
        }

        init {
            text = tabItem.title
            graphic = tabItem.graphic
            content = tabItem.content
            tooltip = buildTooltip()
            contextMenu = buildContextMenu()
        }

        private fun buildTooltip() = Tooltip().apply {
            textProperty().bind(this@TabImpl.textProperty())
        }

        private fun buildContextMenu() = ContextMenu(
            buildCloseItem(),
            buildCloseOtherItem(),
            buildCloseAllItem(),
            buildCloseToLeftItem(),
            buildCloseToRightItem()
        )

        private fun buildCloseItem() = MenuItem().apply {
            text = i18n("tab_view.tab_menu.close")
            setOnAction {
                tabView.closeTabImpl(this@TabImpl)
            }
        }

        private fun buildCloseOtherItem() = MenuItem().apply {
            text = i18n("tab_view.tab_menu.close_other")
            disableProperty().bind(Bindings.size(tabView.tabPane.tabs).lessThan(2))
            setOnAction {
                tabView.tabPane.tabs
                    .filterIsInstance<TabImpl>()
                    .filter { it != this@TabImpl }
                    .forEach(tabView::closeTabImpl)
            }
        }

        private fun buildCloseAllItem() = MenuItem().apply {
            text = i18n("tab_view.tab_menu.close_all")
            setOnAction {
                tabView.tabPane.tabs
                    .filterIsInstance<TabImpl>()
                    .forEach(tabView::closeTabImpl)
            }
        }

        private fun buildCloseToLeftItem() = MenuItem().apply {
            text = i18n("tab_view.tab_menu.close_left")
            disableProperty().bind(indexProperty.isEqualTo(0))
            setOnAction {
                val tabs = tabView.tabPane.tabs.filterIsInstance<TabImpl>()
                val currentIndex = tabs.indexOf(this@TabImpl)
                tabs.dropLast(tabs.size - currentIndex).forEach(tabView::closeTabImpl)
            }
        }

        private fun buildCloseToRightItem() = MenuItem().apply {
            text = i18n("tab_view.tab_menu.close_right")
            disableProperty().bind(Bindings.valueAt(tabView.tabPane.tabs, indexProperty.add(1)).isNull)
            setOnAction {
                val tabs = tabView.tabPane.tabs.filterIsInstance<TabImpl>()
                val currentIndex = tabs.indexOf(this@TabImpl)
                tabs.drop(currentIndex + 1).forEach(tabView::closeTabImpl)
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(TabView::class.java)
    }
}