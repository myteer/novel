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