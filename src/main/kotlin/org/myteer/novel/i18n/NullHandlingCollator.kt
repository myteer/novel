package org.myteer.novel.i18n

import java.text.CollationKey
import java.text.Collator

class NullHandlingCollator(private val baseCollator: Collator) : Collator() {
    override fun hashCode(): Int = baseCollator.hashCode()

    override fun compare(source: String?, target: String?): Int = baseCollator.compare(source ?: "", target ?: "")

    override fun getCollationKey(source: String?): CollationKey = baseCollator.getCollationKey(source)
}