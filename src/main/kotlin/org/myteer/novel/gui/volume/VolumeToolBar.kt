package org.myteer.novel.gui.volume

import javafx.beans.binding.Bindings
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.scene.control.*
import org.myteer.novel.db.data.Chapter
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.control.BiToolBar
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.gui.utils.typeEquals
import org.myteer.novel.i18n.i18n

class VolumeToolBar(
    private val context: Context,
    private val volumeView: VolumeView,
    private val baseItems: ObservableList<Chapter>
) : BiToolBar() {
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
            buildSyncItem(),
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
            val cachedSize = baseItems.filter { true == it.contentCached }.size
            i18n("chapters.cache.info", cachedSize, baseItems.size)
        }, baseItems))
    }

    private fun buildSyncItem() = Button().apply {
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
        graphic = icon("cloud-sync-icon")
        tooltip = Tooltip(i18n("chapters.cloud.sync"))
        setOnAction {
            volumeView.syncChapterListFromCloud()
        }
    }

    private fun buildCacheItem() = Button().apply {
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
        graphic = icon("download-icon")
        tooltip = Tooltip(i18n("chapters.cache.all"))
        setOnAction {
            context.showConfirmationDialog(
                i18n("chapters.cache.all.title"),
                i18n("chapters.cache.all.message")
            ) { btn ->
                if (btn.typeEquals(ButtonType.YES)) {
                    volumeView.cacheAll()
                }
            }
        }
    }

    private fun buildCleanItem() = Button().apply {
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
        graphic = icon("clean-icon")
        tooltip = Tooltip(i18n("chapters.cache.clear"))
        setOnAction {
            context.showConfirmationDialog(
                i18n("chapters.cache.clear.title"),
                i18n("chapters.cache.clear.message")
            ) { btn ->
                if (btn.typeEquals(ButtonType.YES)) {
                    volumeView.clearCache()
                }
            }
        }
    }
}