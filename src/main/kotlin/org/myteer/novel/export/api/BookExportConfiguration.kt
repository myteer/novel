package org.myteer.novel.export.api

import org.myteer.novel.db.data.BookProperty
import java.util.*

abstract class BookExportConfiguration {
    open val availableFields: List<BookProperty<*>>
        get() = BookProperty.allProperties

    var requiredFields: List<BookProperty<*>> = availableFields

    var fieldToSortBy: BookProperty<Comparable<*>>? = null

    var sortLocale: Locale = Locale.forLanguageTag("")

    var reverseItems: Boolean = false
}