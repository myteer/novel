package org.myteer.novel.i18n

import java.text.Collator
import java.util.*

abstract class LanguagePack(val locale: Locale) {
    abstract fun getValues(): ResourceBundle

    abstract fun isRTL(): Boolean

    fun getCollator(): Collator = NullHandlingCollator(Collator.getInstance(locale))

    protected fun getBundle(baseName: String): ResourceBundle = ResourceBundle.getBundle(baseName, locale)
}