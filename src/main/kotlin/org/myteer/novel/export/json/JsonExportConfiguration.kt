package org.myteer.novel.export.json

import org.myteer.novel.db.data.BookProperty
import org.myteer.novel.export.api.BookExportConfiguration

class JsonExportConfiguration : BookExportConfiguration() {
    override val availableFields: List<BookProperty<*>>
        get() = AVAILABLE_FIELDS

    var prettyPrinting = true

    var nonExecutableJson = false

    var serializeNulls = false

    private companion object {
        val AVAILABLE_FIELDS by lazy {
            BookProperty.allProperties - listOf(BookProperty.DESCRIPTION)
        }
    }
}