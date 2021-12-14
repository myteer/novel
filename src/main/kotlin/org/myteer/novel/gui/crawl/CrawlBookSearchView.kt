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

import javafx.scene.Node
import javafx.scene.layout.StackPane
import org.myteer.novel.crawl.vo.BookSearchRequest
import org.myteer.novel.gui.api.Context

open class CrawlBookSearchView(private val context: Context) : StackPane() {
    private val searchForm: CrawlBookSearchForm = buildSearchForm()

    private var content: Node?
        get() = children[0]
        set(value) {
            clearCache()
            children.setAll(value)
        }

    init {
        styleClass.add("crawl-book-search-view")
        buildUI()
    }

    private fun buildUI() {
        content = searchForm
    }

    private fun buildSearchForm() = CrawlBookSearchForm {
        content = buildSearchResultView(it)
    }

    protected fun home() {
        content = searchForm
    }

    open fun buildSearchResultView(request: BookSearchRequest): CrawlBookSearchResultView {
        return CrawlBookSearchResultView(context, request) { home() }
    }

    fun clearCache() {
        children.getOrNull(0).takeIf { it is CrawlBookSearchResultView }?.let {
            (it as CrawlBookSearchResultView).clearCache()
        }
    }
}