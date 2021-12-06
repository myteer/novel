package org.myteer.novel.gui.wizard.segment.lang

import javafx.geometry.Insets
import javafx.scene.control.ListView
import javafx.scene.layout.StackPane
import org.myteer.novel.config.PreferenceKey
import org.myteer.novel.config.Preferences
import org.myteer.novel.i18n.I18N
import java.util.*

class LanguageSegmentView(private val preferences: Preferences) : StackPane() {
    init {
        padding = Insets(10.0)
        buildUI()
    }

    private fun buildUI() {
        children.add(buildListView())
    }

    private fun buildListView() = ListView<LanguageEntry>().apply {
        selectionModel.selectedItemProperty().addListener { _, ov, nv ->
            nv?.let {
                preferences.editor().put(PreferenceKey.LOCALE, it.locale)
                Locale.setDefault(it.locale)
            } ?: selectionModel.select(ov)
        }
        fillListView()
    }

    private fun ListView<LanguageEntry>.fillListView() {
        val availableLocales = I18N.getAvailableLocales().toList()
        val defaultLocaleIndex = availableLocales.indexOf(I18N.defaultLocale())
        items.addAll(availableLocales.map(::LanguageEntry))
        selectionModel.select(defaultLocaleIndex)
        scrollTo(defaultLocaleIndex)
    }

    private class LanguageEntry(val locale: Locale) {
        override fun toString(): String {
            return locale.displayName
        }
    }
}