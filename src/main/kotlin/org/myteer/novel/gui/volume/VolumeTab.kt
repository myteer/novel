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

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Node
import org.myteer.novel.config.Preferences
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.db.data.Book
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.main.BaseTab
import org.myteer.novel.gui.utils.icon

class VolumeTab(
    private val context: Context,
    private val preferences: Preferences,
    private val database: NitriteDatabase,
    private val book: Book
) : BaseTab() {
    override val id = "volume-module-${book.id}"
    override val name = book.name.orEmpty()
    override val icon = icon("library-icon")

    private val content: ObjectProperty<VolumeView> = SimpleObjectProperty()

    override fun buildContent(): Node {
        if (null == content.get()) {
            content.set(VolumeView(context, preferences, database, book.id))
        }
        return content.get()
    }

    override fun destroy(): Boolean {
        content.set(null)
        return true
    }
}
