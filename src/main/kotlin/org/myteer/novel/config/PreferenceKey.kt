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