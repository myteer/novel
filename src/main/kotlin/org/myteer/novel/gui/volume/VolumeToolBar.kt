package org.myteer.novel.gui.volume

import javafx.beans.binding.Bindings
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.scene.control.*
import org.myteer.novel.db.data.Chapter
import org.myteer.novel.gui.control.BiToolBar
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.i18n.i18n

class VolumeToolBar(private val volumeView: VolumeView, private val baseItems: ObservableList<Chapter>) : BiToolBar() {
    init {
        buildUI()
    }

    private fun buildUI() {
        leftItems.addAll(
            buildRefreshItem(),
            Separator(),
            buildCacheInfoLabel()
        )
        rightItems.addAll(
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

    private fun buildCacheInfoLabel() = Label().apply {
        padding = Insets(5.0)
        textProperty().bind(Bindings.createStringBinding({
            val cachedSize = baseItems.filter { !it.content.isNullOrBlank() }.size
            i18n("chapters.cache.info", cachedSize, baseItems.size)
        }, baseItems))
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