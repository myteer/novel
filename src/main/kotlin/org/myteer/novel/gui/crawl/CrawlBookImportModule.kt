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
package org.myteer.novel.gui.crawl

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Node
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.main.Module
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.i18n.i18n

class CrawlBookImportModule(private val context: Context) : Module() {
    override val id = "crawl-book-import"
    override val name = i18n("crawl.book.import.module.title")
    override val icon: Node get() = icon("spider-icon")
    override val preview: Node get() = icon("spider-icon")

    private val content: ObjectProperty<CrawlBookImportView> = SimpleObjectProperty()

    override fun buildContent(): Node {
        if (null == content.get()) {
            content.set(CrawlBookImportView(context))
        }
        return content.get()
    }

    override fun destroy(): Boolean {
        content.get()?.clearCache()
        content.set(null)
        return true
    }
}