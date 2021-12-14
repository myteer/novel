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
package org.myteer.novel.export.api

import org.myteer.novel.db.data.Book
import org.myteer.novel.i18n.I18N

abstract class BaseExporter<C : BookExportConfiguration> : BookExporter<C> {
    protected fun sortBooks(items: List<Book>, config: C): List<Book> {
        val sortedItems = config.fieldToSortBy?.let { field ->
            val collator = I18N.getCollator(config.sortLocale)
            @Suppress("UNCHECKED_CAST")
            items.sortedWith { o1, o2 ->
                val o1Value = field.getValue(o1)
                val o2Value = field.getValue(o2)
                if ((o1Value is String || o2Value is String) && null != collator)
                    collator.compare(o1Value, o2Value)
                else o2Value?.let { (o1Value as? Comparable<Comparable<*>>)?.compareTo(it) } ?: 0
            }
        } ?: items
        return if (config.reverseItems) sortedItems.asReversed() else sortedItems
    }
}