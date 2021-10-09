package org.myteer.novel.gui.preferences.pane

import javafx.css.PseudoClass
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.layout.StackPane
import org.myteer.novel.config.Preferences
import org.myteer.novel.gui.control.KeyBindingDetectionField
import org.myteer.novel.gui.keybinding.KeyBinding
import org.myteer.novel.gui.keybinding.KeyBindings
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.i18n.i18n
import org.slf4j.LoggerFactory

class KeyBindingPane(preferences: Preferences) : PreferencesPane(preferences) {
    override val title: String = i18n("preferences.tab.keybindings")
    override val graphic: Node = icon("keyboard-icon")

    override fun buildContent(): Content = object : Content() {
        init {
            KeyBindings.allKeyBindings().forEach { items.add(buildKeyBindingDetectionControl(it)) }
            items.add(buildRestoreButton())
        }

        private fun buildKeyBindingDetectionControl(keyBinding: KeyBinding): PreferencesControl {
            return PairControl(
                keyBinding.title,
                keyBinding.description(),
                KeyBindingDetectionField(keyBinding.keyCombination).apply {
                    keyCombinationProperty().addListener { _, _, newValue ->
                        keyBinding.keyCombinationProperty.set(newValue)
                        logger.debug("Saving key combination to preferences...")
                        KeyBindings.writeTo(preferences)
                    }
                    keyCombinationProperty().bindBidirectional(keyBinding.keyCombinationProperty)
                }
            )
        }

        private fun buildRestoreButton(): PreferencesControl = Button().run {
            text = i18n("preferences.keybindings.restore_defaults")
            setOnAction {
                KeyBindings.allKeyBindings().forEach {
                    it.keyCombinationProperty.set(it.defaultKeyCombination)
                }
            }
            pseudoClassStateChanged(PseudoClass.getPseudoClass("default"), true)
            SimpleControl(StackPane(this))
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(KeyBindingPane::class.java)
    }
}