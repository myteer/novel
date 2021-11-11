package org.myteer.novel.gui.export.excel

import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.layout.*
import javafx.scene.paint.Color
import jfxtras.styles.jmetro.JMetroStyleClass
import org.myteer.novel.export.excel.ExcelExportConfiguration
import org.myteer.novel.gui.control.FontNamePicker
import org.myteer.novel.gui.export.control.BaseConfigurationView
import org.myteer.novel.gui.utils.*
import org.myteer.novel.i18n.i18n

class ExcelConfigurationView(private val onFinished: (ExcelExportConfiguration) -> Unit) : BorderPane() {
    private val excelExportConfiguration = ExcelExportConfiguration()

    init {
        styleClass.add(JMetroStyleClass.BACKGROUND)
        styleClass.add("excel-configuration-view")
        buildUI()
    }

    private fun buildUI() {
        center = TabView(excelExportConfiguration)
        bottom = ExecuteArea()
    }

    private inner class ExecuteArea : StackPane() {
        init {
            padding = Insets(0.0, 20.0, 20.0, 20.0)
            children.add(buildExecuteButton())
        }

        private fun buildExecuteButton() = Button().apply {
            text = i18n("book.export.execute")
            maxWidth = Double.MAX_VALUE
            isDefaultButton = true
            setOnAction {
                onFinished(excelExportConfiguration)
            }
        }
    }

    private class TabView(private val excelExportConfiguration: ExcelExportConfiguration) : TabPane() {
        init {
            styleClass.add(JMetroStyleClass.UNDERLINE_TAB_PANE)
            buildUI()
        }

        private fun buildUI() {
            tabs.add(tab(i18n("book.export.excel.tab.general"), GeneralView(excelExportConfiguration)))
            tabs.add(tab(i18n("book.export.excel.tab.format"), scrollPane(StyleView(excelExportConfiguration), true)))
        }

        private fun tab(title: String, content: Node) = Tab(title, content).apply {
            isClosable = false
        }
    }

    private class GeneralView(
        config: ExcelExportConfiguration
    ) : BaseConfigurationView<ExcelExportConfiguration>(config) {
        init {
            styleClass.add("general-view")
        }
    }

    private class StyleView(private val config: ExcelExportConfiguration) : GridPane() {
        init {
            styleClass.add("style-view")
            padding = Insets(20.0)
            hgap = 10.0
            vgap = 10.0
            buildUI()
        }

        private fun buildUI() {
            addTextField(i18n("book.export.excel.sheet_name"), config.sheetName, config::sheetName::set)
            addTextField(i18n("book.export.excel.place_holder_text"), config.emptyCellPlaceHolder, config::emptyCellPlaceHolder::set)
            addSection(i18n("book.export.excel.header"), config.headerCellStyle)
            addSection(i18n("book.export.excel.regular"), config.regularCellStyle)
        }

        private inline fun addTextField(label: String, defaultText: String?, crossinline onTextChanged: (String) -> Unit) {
            addRow(Label(label))
            addRow(TextField(defaultText).apply { textProperty().onValuePresent(onTextChanged) })
        }

        private fun addSection(title: String, cellStyle: ExcelExportConfiguration.CellStyle) {
            addRow(Label(title).styleClass("category-label").colspan(2).hgrow(Priority.ALWAYS))
            addRow(Separator().colspan(2).hgrow(Priority.ALWAYS))
            addRow(Label(i18n("book.export.excel.background_color")))
            addRow(BackgroundColorPicker(cellStyle))
            addRow(Label(i18n("book.export.excel.font")))
            addRow(CellFontChooser(cellStyle))
            addRow(Label(i18n("book.export.excel.font_color")))
            addRow(FontColorPicker(cellStyle))
        }

        private class CellFontChooser(private val cellStyle: ExcelExportConfiguration.CellStyle) : HBox(5.0) {
            init {
                GridPane.setHgrow(this, Priority.ALWAYS)
                buildUI()
            }

            private fun buildUI() {
                children.add(buildFontPicker())
                children.add(buildBoldToggle())
                children.add(buildItalicToggle())
                children.add(buildStrikeThroughToggle())
            }

            private fun buildFontPicker() = FontNamePicker().apply {
                setHgrow(this, Priority.ALWAYS)
                maxWidth = Double.MAX_VALUE
                valueProperty().onValuePresent {
                    cellStyle.fontName = it
                }
            }

            private fun buildBoldToggle() = ToggleButton().apply {
                graphic = icon("bold-icon")
                isSelected = cellStyle.isBold
                selectedProperty().onValuePresent { cellStyle.isBold = it }
            }

            private fun buildItalicToggle() = ToggleButton().apply {
                graphic = icon("italic-icon")
                isSelected = cellStyle.isItalic
                selectedProperty().onValuePresent { cellStyle.isItalic = it }
            }

            private fun buildStrikeThroughToggle() = ToggleButton().apply {
                graphic = icon("strikethrough-icon")
                isSelected = cellStyle.isStrikeout
                selectedProperty().onValuePresent { cellStyle.isStrikeout = it }
            }
        }

        private class BackgroundColorPicker(cellStyle: ExcelExportConfiguration.CellStyle) : ColorPicker() {
            init {
                setHgrow(this, Priority.ALWAYS)
                setColumnSpan(this, 2)
                maxWidth = Double.MAX_VALUE
                value = cellStyle.backgroundColor?.toFXColor() ?: Color.TRANSPARENT
                valueProperty().onValuePresent { cellStyle.backgroundColor = it.toAWTColor() }
            }
        }

        private class FontColorPicker(cellStyle: ExcelExportConfiguration.CellStyle) : ColorPicker() {
            init {
                setHgrow(this, Priority.ALWAYS)
                setColumnSpan(this, 2)
                maxWidth = Double.MAX_VALUE
                value = cellStyle.fontColor?.toFXColor() ?: Color.BLACK
                valueProperty().onValuePresent { cellStyle.fontColor = it.toAWTColor() }
            }
        }
    }
}