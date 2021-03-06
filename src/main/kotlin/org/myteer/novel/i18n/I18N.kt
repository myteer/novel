/*
 * Copyright (c) 2021 MTSoftware
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

    fun defaultLocale(): Locale {
        val systemDefault = Locale.getDefault()
        return when {
            getAvailableLocales().contains(systemDefault) -> systemDefault
            else -> Locale.ENGLISH
        }
    }

    fun getAvailableCollators(): Map<Locale, Collator> {
        val map: MutableMap<Locale, Collator> = mutableMapOf()
        loadedLanguagePacks.forEach { (locale, languagePacks) ->
            languagePacks.map(LanguagePack::getCollator).firstOrNull()?.let {
                map[locale] = it
            }
        }
        return map
    }
}

fun i18n(key: String, vararg args: Any) = I18N.getValue(key, *args)