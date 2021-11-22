package org.myteer.novel.gui.volume

import javafx.scene.control.Button
import javafx.scene.control.ContentDisplay
import javafx.scene.control.Tooltip
import org.myteer.novel.gui.control.BiToolBar
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.i18n.i18n

class VolumeToolBar(private val volumeView: VolumeView) : BiToolBar() {
    init {
        buildUI()
    }

    private fun buildUI() {
        leftItems.addAll(
            buildRefreshItem(),
            buildCacheItem(),
            buildCleanItem()
        )
    }

    private fun buildRefreshItem() = Button().apply {
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
        graphic = icon("reload-icon")
        tooltip = Tooltip(i18n("page.reload"))
        setOnAction {
            volumeView.refresh()
        }
    }

    private fun buildCacheItem() = Button().apply {
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
        graphic = icon("download-icon")
        tooltip = Tooltip(i18n("chapters.cache.all"))
        setOnAction {
            volumeView.cacheAll()
        }
    }

    private fun buildCleanItem() = Button().apply {
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
        graphic = icon("clean-icon")
        tooltip = Tooltip(i18n("chapters.cache.clear"))
        setOnAction {
            volumeView.clearCache()
        }
    }
}