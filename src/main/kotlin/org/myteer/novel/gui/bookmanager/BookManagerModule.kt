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
package org.myteer.novel.gui.bookmanager

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Node
import org.myteer.novel.config.Preferences
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.main.Module
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.i18n.i18n

class BookManagerModule(
    private val context: Context,
    private val preferences: Preferences,
    private val database: NitriteDatabase
) : Module() {
    override val id = "book-manager-module"
    override val name = i18n("book.manager.module.title")
    override val icon: Node get() = icon("library-icon")
    override val preview: Node get() = icon("library-icon")

    private val content: ObjectProperty<BookManagerView> = SimpleObjectProperty()

    override fun buildContent(): Node {
        if (null == content.get()) {
            content.set(BookManagerView(context, preferences, database))
        }
        return content.get()
    }

    override fun destroy(): Boolean {
        content.get()?.clearCache()
        content.set(null)
        return true
    }

    override fun sendMessage(message: Message) {
        content.get()?.let { view ->
            when (message) {
                is BookImportRequest -> view.importBook(message.bookId)
            }
        }
    }

    class BookImportRequest(val bookId: String) : Message
}
