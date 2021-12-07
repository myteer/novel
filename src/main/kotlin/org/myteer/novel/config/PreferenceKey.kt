package org.myteer.novel.config

import org.myteer.novel.config.login.LoginData
import org.myteer.novel.config.login.LoginDataAdapter
import org.myteer.novel.config.theme.ThemeAdapter
import org.myteer.novel.gui.theme.Theme
import java.time.LocalDateTime
import java.util.*
import java.util.function.Supplier

class PreferenceKey<T>(
    val jsonKey: String,
    val type: Class<T>,
    val defaultValue: Supplier<T>,
    val configAdapter: ConfigAdapter<T>? = null
) {
    companion object {
        @JvmField
        val LOCALE = PreferenceKey("locale", Locale::class.java, Locale::getDefault)

        @JvmField
        val LOGIN_DATA = PreferenceKey("loginData", LoginData::class.java, LoginData::empty, LoginDataAdapter())

        @JvmField
        val THEME = PreferenceKey("theme", Theme::class.java, Theme::getDefault, ThemeAdapter())

        @JvmField
        val SEARCH_UPDATES = PreferenceKey("searchUpdates", Boolean::class.java, { true })

        @JvmField
        val LAST_UPDATE_SEARCH = PreferenceKey("searchUpdates.last", LocalDateTime::class.java, { LocalDateTime.MIN })
    }
}