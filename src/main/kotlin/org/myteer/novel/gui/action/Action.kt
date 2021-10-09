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