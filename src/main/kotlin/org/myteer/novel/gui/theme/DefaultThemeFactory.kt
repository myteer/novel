package org.myteer.novel.gui.theme

object DefaultThemeFactory {
    fun get(): Theme = OsSynchronizedTheme()
}