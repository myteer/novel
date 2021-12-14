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