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
package org.myteer.novel.gui.preferences.pane

import javafx.scene.Node
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Slider
import javafx.util.StringConverter
import org.myteer.novel.config.PreferenceKey
import org.myteer.novel.config.Preferences
import org.myteer.novel.gui.theme.Theme
import org.myteer.novel.gui.theme.ThemeMeta
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.gui.window.BaseWindow
import org.myteer.novel.i18n.i18n
import org.myteer.novel.utils.ReflectionUtils
import org.slf4j.LoggerFactory

class AppearancePane(preferences: Preferences) : PreferencesPane(preferences) {
    override val title: String = i18n("preferences.tab.appearance")
    override val graphic: Node = icon("paint-icon")

    override fun buildContent(): Content = object : Content() {
        init {
            items.add(buildThemeSelect())
            items.add(buildWindowOpacitySlider())
        }

        private fun buildThemeSelect(): PreferencesControl {
            return ChoiceBox<ThemeMeta<*>>().run {
                converter = object : StringConverter<ThemeMeta<*>?>() {
                    override fun toString(themeMeta: ThemeMeta<*>?): String {
                        return themeMeta?.displayNameSupplier?.get() ?: ""
                    }

                    override fun fromString(string: String?): ThemeMeta<*>? {
                        return null
                    }
                }

                Theme.getAvailableThemesData().forEach {
                    items.add(it)
                    if (Theme.getDefault().javaClass == it.themeClass) {
                        selectionModel.select(it)
                    }
                }

                selectionModel.selectedItemProperty().addListener{ _, _, it ->
                    try {
                        val theme = ReflectionUtils.constructObject(it.themeClass)
                        logger.debug("The theme object: {}", theme)
                        Theme.setDefault(theme)
                        preferences.editor().put(PreferenceKey.THEME, theme)
                    } catch (e: Exception) {
                        logger.error("Couldn't set the theme", e)
                    }
                }

                PairControl(
                    i18n("preferences.appearance.theme"),
                    i18n("preferences.appearance.theme.desc"),
                    this
                )
            }
        }

        private fun buildWindowOpacitySlider(): PreferencesControl {
            return Slider(20.0, 100.0, BaseWindow.globalOpacity.value * 100).run {
                valueProperty().addListener { _, _, value ->
                    value.toDouble().div(100).let(BaseWindow.globalOpacity::set)
                }
                valueChangingProperty().addListener { _, _, changing ->
                    changing.takeIf { it.not() }?.let {
                        logger.debug("Global opacity saved to configurations")
                        preferences.editor().put(BaseWindow.GLOBAL_OPACITY_CONFIG_KEY, value.div(100))
                    }
                }
                PairControl(
                    i18n("preferences.appearance.window_opacity"),
                    i18n("preferences.appearance.window_opacity.desc"),
                    this
                )
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AppearancePane::class.java)
    }
}