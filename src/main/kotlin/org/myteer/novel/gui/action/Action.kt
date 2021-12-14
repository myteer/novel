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
package org.myteer.novel.gui.action

import org.myteer.novel.config.Preferences
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.entry.DatabaseTracker
import org.myteer.novel.gui.keybinding.KeyBinding
import org.myteer.novel.i18n.i18n

class Action(
    private val i18nName: String,
    val iconStyleClass: String,
    val keyBinding: KeyBinding? = null,
    private val operation: (Context, Preferences, DatabaseTracker) -> Unit
) {
    private var displayNameBacking: String? = null
    val displayName: String
        get() = displayNameBacking ?: i18n(i18nName).also { displayNameBacking = it }

    fun invoke(context: Context, preferences: Preferences, databaseTracker: DatabaseTracker) {
        operation(context, preferences, databaseTracker)
    }
}