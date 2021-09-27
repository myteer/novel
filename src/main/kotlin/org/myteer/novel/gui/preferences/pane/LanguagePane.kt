package org.myteer.novel.gui.preferences.pane

import javafx.scene.Node
import javafx.scene.control.ButtonType
import javafx.scene.control.ChoiceBox
import javafx.util.StringConverter
import org.myteer.novel.config.PreferenceKey
import org.myteer.novel.config.Preferences
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.gui.utils.typeEquals
import org.myteer.novel.i18n.I18N
import org.myteer.novel.i18n.i18n
import org.myteer.novel.main.ApplicationRestart
import java.util.*

class LanguagePane(private val context: Context, preferences: Preferences) : PreferencesPane(preferences) {
    override val title: String = i18n("preferences.tab.language")
    override val graphic: Node = icon("translate-icon")

    override fun buildContent(): Content = object : Content() {
        init {
            items.add(buildLanguageSelectControl())
        }

        private fun buildLanguageSelectControl(): PreferencesControl {
            return ChoiceBox<Locale>().run {
                converter = object : StringConverter<Locale>() {
                    override fun toString(locale: Locale?): String = locale?.displayLanguage ?: ""
                    override fun fromString(string: String?): Locale = Locale.forLanguageTag(string)
                }

                I18N.getAvailableLocales().forEach {
                    items.add(it)
                    if (it == preferences.get(PreferenceKey.LOCALE)) {
                        selectionModel.select(it)
                    }
                }

                selectionModel.selectedItemProperty().addListener{ _, _, locale ->
                    preferences.editor().put(PreferenceKey.LOCALE, locale)
                    context.showConfirmationDialog(
                        i18n("app.lang.restart.title"),
                        i18n("app.lang.restart.message")
                    ) {
                        if (it.typeEquals(ButtonType.YES)) {
                            ApplicationRestart.restart()
                        }
                    }
                }

                PairControl(
                    i18n("preferences.language.lang"),
                    i18n("preferences.language.lang.desc"),
                    this
                )
            }
        }
    }
}