package org.myteer.novel.config

import org.myteer.novel.config.login.LoginData
import org.myteer.novel.config.login.LoginDataAdapter
import org.myteer.novel.gui.theme.Theme
import org.myteer.novel.config.theme.ThemeAdapter
import java.util.*
import java.util.function.Supplier

class PreferenceKey<T>(
    val jsonKey: String,
    val type: Class<T>,
    val defaultValue: Supplier<T>,
    val configAdapter: ConfigAdapter<T>? = null
) {
    companion object {
        val LOCALE = PreferenceKey("locale", Locale::class.java, Locale::getDefault)
        val LOGIN_DATA = PreferenceKey("loginData", LoginData::class.java, LoginData::empty, LoginDataAdapter())
        val THEME = PreferenceKey("theme", Theme::class.java, Theme::getDefault, ThemeAdapter())
    }
}