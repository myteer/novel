package org.myteer.novel.gui.keybinding

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import org.myteer.novel.config.PreferenceKey
import org.myteer.novel.config.Preferences
import org.myteer.novel.config.keybindings.KeyBindingsAdapter
import org.myteer.novel.i18n.i18n

object KeyBindings {
    private val CONFIG_KEY = PreferenceKey("default.key.bindings", KeyBindings::class.java, { this }, KeyBindingsAdapter())

    val createDatabase: KeyBinding = KeyBinding(
        id = "createDatabaseKeyBinding",
        title = i18n("action.create_database"),
        description = { i18n("preferences.keybindings.create_database.desc") },
        winLinuxKeyCombination = KeyCodeCombination(KeyCode.C, KeyCombination.ALT_DOWN, KeyCombination.CONTROL_DOWN),
        macKeyCombination = KeyCodeCombination(KeyCode.C, KeyCombination.ALT_DOWN, KeyCombination.META_DOWN)
    )

    val openDatabase: KeyBinding = KeyBinding(
        id = "openDatabaseKeyBinding",
        title = i18n("action.open_database"),
        description = { i18n("preferences.keybindings.open_database.desc") },
        winLinuxKeyCombination = KeyCodeCombination(KeyCode.O, KeyCombination.ALT_DOWN, KeyCombination.CONTROL_DOWN),
        macKeyCombination = KeyCodeCombination(KeyCode.O, KeyCombination.ALT_DOWN, KeyCombination.META_DOWN)
    )

    val openDatabaseManager: KeyBinding = KeyBinding(
        id = "openDatabaseManagerKeyBinding",
        title = i18n("action.open_database_manager"),
        description = { i18n("preferences.keybindings.open_database_manager.desc") },
        winLinuxKeyCombination = KeyCodeCombination(KeyCode.M, KeyCombination.ALT_DOWN, KeyCombination.CONTROL_DOWN),
        macKeyCombination = KeyCodeCombination(KeyCode.M, KeyCombination.ALT_DOWN, KeyCombination.META_DOWN)
    )

    val saveChanges: KeyBinding = KeyBinding(
        id = "saveChangesKeyBinding",
        title = i18n("action.save_changes"),
        description = { i18n("preferences.keybindings.save_changes.desc") },
        winLinuxKeyCombination = KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN),
        macKeyCombination = KeyCodeCombination(KeyCode.S, KeyCombination.META_DOWN)
    )

    val openSettings: KeyBinding = KeyBinding(
        id = "openSettingsKeyBinding",
        title = i18n("action.settings"),
        description = { i18n("preferences.keybindings.open_settings.desc") },
        winLinuxKeyCombination = KeyCodeCombination(KeyCode.S, KeyCombination.ALT_DOWN, KeyCombination.CONTROL_DOWN),
        macKeyCombination = KeyCodeCombination(KeyCode.S, KeyCombination.ALT_DOWN, KeyCombination.META_DOWN)
    )

    val fullScreen: KeyBinding = KeyBinding(
        id = "fullScreenKeyBinding",
        title = i18n("action.full_screen"),
        description = { i18n("preferences.keybindings.full_screen.desc") },
        defaultKeyCombination = KeyCodeCombination(KeyCode.F11)
    )

    val newEntry: KeyBinding = KeyBinding(
        id = "newEntryKeyBinding",
        title = i18n("action.new_entry"),
        description = { i18n("preferences.keybindings.new_entry.desc") },
        winLinuxKeyCombination = KeyCodeCombination(KeyCode.N, KeyCombination.SHIFT_DOWN, KeyCombination.CONTROL_DOWN),
        macKeyCombination = KeyCodeCombination(KeyCode.N, KeyCombination.SHIFT_DOWN, KeyCombination.META_DOWN)
    )

    val restartApplication: KeyBinding = KeyBinding(
        id = "restartApplicationKeyBinding",
        title = i18n("action.restart"),
        description = { i18n("preferences.keybindings.restart.desc") },
        winLinuxKeyCombination = KeyCodeCombination(KeyCode.R, KeyCombination.SHIFT_DOWN, KeyCombination.CONTROL_DOWN),
        macKeyCombination = KeyCodeCombination(KeyCode.R, KeyCombination.SHIFT_DOWN, KeyCombination.META_DOWN)
    )

    val findRecord: KeyBinding = KeyBinding(
        id = "findRecordKeyBinding",
        title = i18n("preferences.keybindings.find_record"),
        description = { i18n("preferences.keybindings.find_record.desc") },
        winLinuxKeyCombination = KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN),
        macKeyCombination = KeyCodeCombination(KeyCode.F, KeyCombination.META_DOWN)
    )

    val deleteRecord: KeyBinding = KeyBinding(
        id = "deleteRecordKeyBinding",
        title = i18n("preferences.keybindings.delete_record"),
        description = { i18n("preferences.keybindings.delete_record.desc") },
        defaultKeyCombination = KeyCodeCombination(KeyCode.DELETE)
    )

    val refreshPage: KeyBinding = KeyBinding(
        id = "refreshPageKeyBinding",
        title = i18n("page.reload"),
        description = { "" },
        defaultKeyCombination = KeyCodeCombination(KeyCode.F5)
    )

    fun allKeyBindings(): List<KeyBinding> = javaClass.declaredFields
            .filter { it.type == KeyBinding::class.java }
            .map { it.get(this) as KeyBinding }
            .toList()

    fun loadFrom(preferences: Preferences) {
        preferences.get(CONFIG_KEY)
    }

    fun writeTo(preferences: Preferences) {
        preferences.editor().put(CONFIG_KEY, this)
    }
}