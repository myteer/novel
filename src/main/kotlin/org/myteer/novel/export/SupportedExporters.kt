package org.myteer.novel.export

import okhttp3.internal.toImmutableList
import org.myteer.novel.export.api.BookExporter
import org.myteer.novel.export.excel.ExcelExporter
import org.myteer.novel.export.json.JsonExporter
import java.util.*

object SupportedExporters : List<BookExporter<*>> by LinkedList(loadBuildInExporters()).toImmutableList()

private fun loadBuildInExporters() = listOf(
    JsonExporter(),
    ExcelExporter()
)