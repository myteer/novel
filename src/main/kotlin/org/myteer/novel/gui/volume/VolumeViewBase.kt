package org.myteer.novel.gui.volume

import com.google.common.collect.LinkedHashMultimap
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.control.Hyperlink
import javafx.scene.control.Label
import javafx.scene.layout.TilePane
import javafx.scene.layout.VBox
import jfxtras.styles.jmetro.JMetroStyleClass
import org.myteer.novel.config.Preferences
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.db.data.Chapter
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.gui.utils.styleClass
import org.myteer.novel.gui.volume.overlay.ChapterOverlay

class VolumeViewBase(
    private val context: Context,
    private val preferences: Preferences,
    private val database: NitriteDatabase,
    private val baseItems: ObservableList<Chapter>
) : VBox() {
    init {
        styleClass.add("volume-view")
        buildUI()
    }

    private fun buildUI() {
        buildVolumeList(baseItems.toList()).map(::buildVolumePane).let(children::setAll)
        baseItems.addListener(ListChangeListener {
            buildVolumeList(baseItems.toList()).map(::buildVolumePane).let(children::setAll)
        })
    }

    private fun buildVolumePane(volume: Volume) = VBox().apply {
        styleClass.add("volume-pane")
        alignment = Pos.TOP_CENTER
        children.add(Label(volume.name).styleClass(JMetroStyleClass.BACKGROUND).styleClass("volume-name"))
        children.add(TilePane().also { pane ->
            pane.styleClass.add("chapter-pane")
            volume.chapters.map { chapter ->
                Hyperlink(chapter.name).apply {
                    styleClass.add("chapter-item")
                    if (!chapter.content.isNullOrBlank()) {
                        graphic = icon("check-circle-outline-icon")
                    }
                    setOnAction {
                        context.showOverlay(ChapterOverlay(context, preferences, database, chapter.bookId!!, chapter.id!!))
                    }
                }
            }.forEach(pane.children::add)
        })
    }

    private fun buildVolumeList(chapters: List<Chapter>): List<Volume> {
        val map = LinkedHashMultimap.create<Volume, Chapter>()
        chapters.sorted().forEach {
            map.put(Volume(it.volumeIndex, it.volumeName), it)
        }
        return map.asMap().entries.map { it.key.setChapters(it.value) }
    }

    private class Volume(val index: Int, val name: String?) {
        val chapters: MutableList<Chapter> = mutableListOf()

        fun setChapters(items: Collection<Chapter>) = apply {
            chapters.clear()
            chapters.addAll(items)
        }

        override fun hashCode(): Int = index.hashCode()

        override fun equals(other: Any?): Boolean {
            return if (other is Volume) {
                index == other.index
            } else false
        }
    }
}