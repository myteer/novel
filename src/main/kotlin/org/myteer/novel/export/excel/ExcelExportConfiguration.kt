package org.myteer.novel.export.excel

import org.myteer.novel.db.data.BookProperty
import org.myteer.novel.export.api.BookExportConfiguration
import java.awt.Color

class ExcelExportConfiguration : BookExportConfiguration() {
    override val availableFields: List<BookProperty<*>>
        get() = AVAILABLE_FIELDS

    var sheetName: String? = "Books"

    var emptyCellPlaceHolder: String? = "-"

    var headerCellStyle = CellStyle(isBold = true)

    var regularCellStyle = CellStyle()

    class CellStyle(
        var isBold: Boolean = false,
        var isItalic: Boolean = false,
        var isStrikeout: Boolean = false,
        var underline: Byte = 0,
        var fontName: String? = null,
        var fontSize: Short? = null,
        var fontColor: Color? = null,
        var backgroundColor: Color? = null
    )

    private companion object {
        val AVAILABLE_FIELDS by lazy {
            BookProperty.allProperties - listOf(BookProperty.THUMBNAIL)
        }
    }
}