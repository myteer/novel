package org.myteer.novel.i18n

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.text.Collator
import java.text.MessageFormat
import java.util.*

object I18N {
    private val logger: Logger = LoggerFactory.getLogger(I18N::class.java)
    private val loadedLanguagePacks = mutableMapOf<Locale, MutableList<LanguagePack>>()
    private val defaultLanguagePack = EnglishLanguagePack()
    private var currentLanguagePack: LanguagePack? = null

    init {
        registerBaseLanguagePack()
    }

    private fun registerBaseLanguagePack() {
        putLanguagePack(Locale.ENGLISH, EnglishLanguagePack())
        putLanguagePack(Locale.SIMPLIFIED_CHINESE, SimpleChineseLanguagePack())
    }

    private fun putLanguagePack(locale: Locale, languagePack: LanguagePack) {
        loadedLanguagePacks[locale] = loadedLanguagePacks.getOrDefault(locale, ArrayList()).apply {
            add(languagePack)
        }
    }

    fun getValue(key: String, vararg args: Any): String {
        return try {
            getValue(getValues(), key, *args)
        } catch (e: MissingResourceException) {
            logger.error("couldn't find i18n value", e)
            key
        }
    }

    private fun getValue(resourceBundle: ResourceBundle, key: String, vararg args: Any): String {
        return if (args.isEmpty()) {
            resourceBundle.getString(key)
        } else {
            getFormat(resourceBundle, key, *args)
        }
    }

    private fun getFormat(resourceBundle: ResourceBundle, key: String, vararg args: Any): String {
        return MessageFormat.format(resourceBundle.getString(key), *args)
    }

    fun getValues(): ResourceBundle {
        recognizeCurrentLanguagePack()
        return currentLanguagePack!!.getValues()
    }

    private fun recognizeCurrentLanguagePack() {
        if (null == currentLanguagePack || Locale.getDefault() != currentLanguagePack?.locale) {
            currentLanguagePack = getLanguagePackForLocale(Locale.getDefault()) ?: defaultLanguagePack
        }
    }

    private fun getLanguagePackForLocale(locale: Locale): LanguagePack? {
        return loadedLanguagePacks[locale]?.get(0)
    }

    fun getCollator(locale: Locale): Collator? {
        return getLanguagePackForLocale(locale)?.getCollator()
    }

    fun getAvailableLocales(): Set<Locale> = loadedLanguagePacks.keys
}

fun i18n(key: String, vararg args: Any) = I18N.getValue(key, *args)