package org.myteer.novel.gui.theme

interface Themeable {
    fun handleThemeApply(newTheme: Theme, oldTheme: Theme = Theme.empty())
}