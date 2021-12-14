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
package org.myteer.novel.gui.volume

import com.google.common.collect.LinkedHashMultimap
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.control.Hyperlink
import javafx.scene.control.Label
import javafx.scene.layout.TilePane
import javafx.scene.layout.VBox
import org.myteer.novel.config.Preferences
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.db.data.Chapter
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.gui.utils.styleClass
import org.myteer.novel.gui.volume.chapter.ChapterActivity

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
        children.add(Label(volume.name).styleClass("volume-name"))
        children.add(TilePane().also { pane ->
            pane.styleClass.add("chapter-pane")
            volume.chapters.map { chapter ->
                Hyperlink(chapter.name).apply {
                    styleClass.add("chapter-item")
                    if (true == chapter.contentCached) {
                        graphic = icon("check-circle-outline-icon")
                    }
                    setOnAction {
                        ChapterActivity(preferences, database, chapter.bookId, chapter.id).run {
                            show(context.getContextWindow())
                        }
                    }
                }
            }.forEach(pane.children::add)
        })
    }

    private fun buildVolumeList(chapters: List<Chapter>): List<Volume> {
        val map = LinkedHashMultimap.create<Volume, Chapter>()
        val volumeMap = mutableMapOf<Int, Volume>()
        chapters.forEach {
            map.put(volumeMap.getOrPut(it.volumeIndex) { Volume(it.volumeIndex, it.volumeName) }, it)
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