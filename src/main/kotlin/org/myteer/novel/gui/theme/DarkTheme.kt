package org.myteer.novel.gui.theme

import jfxtras.styles.jmetro.Style
import org.myteer.novel.i18n.i18n

class DarkTheme : JMetroTheme(Style.DARK) {
    companion object {
        fun getMeta() = ThemeMeta(DarkTheme::class.java) { i18n("app.ui.theme.dark") }
    }

    override fun additionalStyleSheets(): List<String> {
        return DarkTheme::class.java.getResource("dark.css")?.toExternalForm()?.let { listOf(it) } ?: listOf()
    }
}