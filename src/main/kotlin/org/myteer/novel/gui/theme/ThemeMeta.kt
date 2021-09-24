package org.myteer.novel.gui.theme

import java.util.function.Supplier

data class ThemeMeta<T : Theme>(
    val themeClass: Class<T>,
    val displayNameSupplier: Supplier<String>
) {
    val displayName: String
        get() = displayNameSupplier.get()
}