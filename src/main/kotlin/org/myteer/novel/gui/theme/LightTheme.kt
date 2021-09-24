package org.myteer.novel.gui.theme

import jfxtras.styles.jmetro.Style
import org.myteer.novel.i18n.i18n

class LightTheme : JMetroTheme(Style.LIGHT) {
    companion object {
        fun getMeta() = ThemeMeta(LightTheme::class.java) { i18n("app.ui.theme.light") }
    }

    override fun additionalStyleSheets(): List<String> {
        return LightTheme::class.java.getResource("light.css")?.toExternalForm()?.let { listOf(it) } ?: listOf()
    }
}