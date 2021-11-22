package org.myteer.novel.gui.volume.overlay

import javafx.beans.binding.Bindings
import javafx.beans.property.ObjectProperty
import javafx.scene.control.Button
import javafx.scene.control.ColorPicker
import javafx.scene.control.Spinner
import org.myteer.novel.config.Preferences
import org.myteer.novel.db.data.Chapter
import org.myteer.novel.gui.control.BiToolBar
import org.myteer.novel.gui.control.FontPicker
import org.myteer.novel.gui.utils.onValuePresent
import org.myteer.novel.gui.volume.overlay.ChapterShowConfiguration.Companion.CHAPTER_SHOW_CONFIG_KEY
import org.myteer.novel.i18n.i18n
import java.util.*

class ChapterShowToolBar(
    private val preferences: Preferences,
    private val configuration: ChapterShowConfiguration,
    private val chapterProperty: ObjectProperty<Chapter>,
    private val view: ChapterLoadPane
) : BiToolBar() {
    init {
        styleClass.add("chapter-tool-bar")
        buildUI()
    }

    private fun buildUI() {
        leftItems.addAll(
            buildFontPicker(),
            buildFontSizeSpinner(),
            buildFontColorPicker()
        )
        rightItems.addAll(
            buildPreviousChapterButton(),
            buildNextChapterButton()
        )
    }

    private fun buildPreviousChapterButton() = Button().apply {
        text = i18n("chapter.previous.title")
        disableProperty().bind(Bindings.createBooleanBinding({
            chapterProperty.value?.previousId?.let {
                it.isEmpty() || "-1" == it
            } ?: true
        }, chapterProperty))
        setOnAction {
            view.loadData(chapterProperty.value.bookId!!, chapterProperty.value.previousId!!)
        }
    }

    private fun buildNextChapterButton() = Button().apply {
        text = i18n("chapter.next.title")
        disableProperty().bind(Bindings.createBooleanBinding({
            chapterProperty.value?.nextId?.let {
                it.isEmpty() || "-1" == it
            } ?: true
        }, chapterProperty))
        setOnAction {
            view.loadData(chapterProperty.value.bookId!!, chapterProperty.value.nextId!!)
        }
    }

    private fun buildFontPicker() = FontPicker().apply {
        selectByFamily(configuration.fontFamilyProperty.value)
        selectionModel.selectedItemProperty().onValuePresent {
            configuration.fontFamilyProperty.set(it.getFamily(Locale.ENGLISH))
            preferences.editor().put(CHAPTER_SHOW_CONFIG_KEY, configuration)
        }
    }

    private fun buildFontSizeSpinner() = Spinner<Int>(8, 30, configuration.fontSizeProperty.value).apply {
        prefWidth = 70.0
        valueProperty().onValuePresent {
            configuration.fontSizeProperty.set(it)
            preferences.editor().put(CHAPTER_SHOW_CONFIG_KEY, configuration)
        }
    }

    private fun buildFontColorPicker() = ColorPicker(configuration.fontColorProperty.value).apply {
        valueProperty().onValuePresent {
            configuration.fontColorProperty.set(it)
            preferences.editor().put(CHAPTER_SHOW_CONFIG_KEY, configuration)
        }
    }
}