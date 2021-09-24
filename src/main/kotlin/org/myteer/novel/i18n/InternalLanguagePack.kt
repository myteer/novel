package org.myteer.novel.i18n

import java.util.*

abstract class InternalLanguagePack(locale: Locale) : LanguagePack(locale) {
    companion object {
        private const val BASE_NAME = "org.myteer.novel.i18n.messages"
    }

    override fun getValues(): ResourceBundle {
        return getBundle(BASE_NAME)
    }
}