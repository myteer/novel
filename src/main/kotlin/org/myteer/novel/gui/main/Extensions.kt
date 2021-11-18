package org.myteer.novel.gui.main

import org.myteer.novel.gui.control.tabview.TabItem

fun Module.getTabItem() = TabItem(
    this@getTabItem.id,
    this@getTabItem.name,
    { this@getTabItem.icon },
    { this@getTabItem.activate() },
    { this@getTabItem.close() }
)

fun BaseTab.getTabItem() = TabItem(
    this@getTabItem.id,
    this@getTabItem.name,
    { this@getTabItem.icon },
    { this@getTabItem.activate() },
    { this@getTabItem.close() }
)