package org.myteer.novel.export.excel

import javafx.scene.Node
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.Font
import org.apache.poi.ss.util.WorkbookUtil
import org.apache.poi.xssf.streaming.SXSSFSheet
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.myteer.novel.db.data.Book
import org.myteer.novel.export.api.BaseExporter
import org.myteer.novel.export.api.ExportProcessObserver
import org.myteer.novel.gui.export.ConfigurationDialog
import org.myteer.novel.gui.export.excel.ExcelConfigurationDialog
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.i18n.i18n
import java.awt.Color
import java.io.OutputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class ExcelExporter : BaseExporter<ExcelExportConfiguration>() {
    override val name = "Excel"
    override val icon: Node get() = icon("excel-icon")
    override val configurationDialog: ConfigurationDialog<ExcelExportConfiguration> get() = ExcelConfigurationDialog()
    override val contentType = "xlsx"
    override val contentTypeDescription = i18n("file.content_type.desc.excel")

    override fun write(
        items: List<Book>,
        output: OutputStream,
        config: ExcelExportConfiguration,
        observer: ExportProcessObserver
    ) {
        val sortedItems = sortBooks(items, config)
        buildWorkbook(sortedItems, config).use { workbook ->
            output.buffered().use(workbook::write)
            workbook.dispose()
        }
    }

    private fun buildWorkbook(items: List<Book>, config: ExcelExportConfiguration): SXSSFWorkbook {
        val workbook = SXSSFWorkbook()

        val sheet: SXSSFSheet =
            config.sheetName
                ?.let(WorkbookUtil::createSafeSheetName)
                ?.let(workbook::createSheet)
                ?: workbook.createSheet()

        val createdRowCount = createHeaderRows(workbook, sheet, config)
        createRegularRows(workbook, sheet, config, items, createdRowCount)

        sheet.autoSizeColumns(config.requiredFields.indices)

        return workbook
    }

    private fun createHeaderRows(workbook: SXSSFWorkbook, sheet: SXSSFSheet, config: ExcelExportConfiguration): Int {
        val row = sheet.createRow(0).apply { height = -1 }
        val cellStyle = config.headerCellStyle.asPoiCellStyle(workbook.xssfWorkbook)
        config.requiredFields.forEachIndexed { index, field ->
            row.createCell(index).apply {
                setCellStyle(cellStyle)
                setCellValue(field.name)
            }
            sheet.trackColumnForAutoSizing(index)
        }
        return 1
    }

    private fun createRegularRows(
        workbook: SXSSFWorkbook,
        sheet: SXSSFSheet,
        config: ExcelExportConfiguration,
        items: List<Book>,
        initialRowCount: Int
    ) {
        val cellStyle = config.regularCellStyle.asPoiCellStyle(workbook.xssfWorkbook)
        items.forEachIndexed { index, book ->
            createRowForBook(sheet, config, cellStyle, book, index + initialRowCount)
        }
    }

    private fun createRowForBook(
        sheet: SXSSFSheet,
        config: ExcelExportConfiguration,
        cellStyle: CellStyle,
        book: Book,
        rowIndex: Int
    ) {
        val row = sheet.createRow(rowIndex).apply { height = -1 }
        config.requiredFields.forEachIndexed { index, field ->
            row.createCell(index).apply {
                setCellStyle(cellStyle)
                setValue(field.getValue(book), config.emptyCellPlaceHolder)
            }
        }
    }

    private fun ExcelExportConfiguration.CellStyle.asPoiCellStyle(workbook: XSSFWorkbook): CellStyle =
        workbook.createCellStyle().apply {
            setFont(getPoiFont(workbook))
            backgroundColor?.let {
                fillPattern = FillPatternType.SOLID_FOREGROUND
                setFillForegroundColor(it.asXSSFColor(workbook))
            }
        }

    private fun ExcelExportConfiguration.CellStyle.getPoiFont(workbook: XSSFWorkbook): Font =
        workbook.createFont().apply {
            bold = this@getPoiFont.isBold
            italic = this@getPoiFont.isItalic
            strikeout = this@getPoiFont.isStrikeout
            underline = this@getPoiFont.underline
            this@getPoiFont.fontName?.let { fontName = it }
            this@getPoiFont.fontSize?.let { fontHeightInPoints = it }
            this@getPoiFont.fontColor?.let { setColor(it.asXSSFColor(workbook)) }
        }

    private fun Color.asXSSFColor(workbook: XSSFWorkbook): XSSFColor =
        XSSFColor(this, workbook.stylesSource.indexedColors)

    private fun Cell.setValue(value: Any?, valueIfNull: String?) {
        when (value) {
            is String -> setCellValue(value)
            is Boolean -> setCellValue(value)
            is Double -> setCellValue(value)
            is Date -> setCellValue(value)
            is LocalDate -> setCellValue(value)
            is LocalDateTime -> setCellValue(value)
            is Calendar -> setCellValue(value)
            is Locale -> setCellValue(value.displayLanguage)
            is List<*> -> setCellValue(value.joinToString(","))
            else -> setCellValue(value?.toString() ?: valueIfNull)
        }
    }

    private fun SXSSFSheet.autoSizeColumns(indices: IntRange) {
        indices.forEach(this::autoSizeColumn)
    }
}