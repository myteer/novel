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
package org.myteer.novel.gui.wizard.segment.theme

import javafx.geometry.Insets
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.control.ContentDisplay
import javafx.scene.control.RadioButton
import javafx.scene.control.ToggleGroup
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import org.myteer.novel.config.PreferenceKey
import org.myteer.novel.config.Preferences
import org.myteer.novel.gui.theme.DarkTheme
import org.myteer.novel.gui.theme.LightTheme
import org.myteer.novel.gui.theme.OsSynchronizedTheme
import org.myteer.novel.gui.theme.Theme
import org.myteer.novel.gui.utils.onValuePresent
import org.myteer.novel.i18n.i18n
import org.slf4j.LoggerFactory

class ThemeSegmentView(private val preferences: Preferences) : StackPane() {
    init {
        buildUI()
    }

    private fun buildUI() {
        children.add(Group(buildHBox()))
    }

    private fun buildHBox() = HBox(10.0).apply {
        buildRadioGroup().also { group ->
            buildToggle(
                "segment.theme.light",
                "/org/myteer/novel/image/wizard/ThemeLight.png",
                group,
                LightTheme::class.java
            ) { LightTheme() }.let(children::add)

            buildToggle(
                "segment.theme.dark",
                "/org/myteer/novel/image/wizard/ThemeDark.png",
                group,
                DarkTheme::class.java
            ) { DarkTheme() }.let(children::add)

            buildToggle(
                "segment.theme.sync",
                "/org/myteer/novel/image/wizard/ThemeSynchronized.png",
                group,
                OsSynchronizedTheme::class.java
            ) { OsSynchronizedTheme() }.let(children::add)
        }
    }

    private fun <T : Theme> buildToggle(
        i18n: String,
        thumbnailPath: String,
        group: ToggleGroup,
        themeClass: Class<T>,
        themeFactory: () -> T
    ) = ThemeToggle(
        i18n(i18n),
        ImageView(thumbnailPath).apply {
            fitWidth = 250.0
            fitHeight = 207.0
        },
        themeClass,
        themeFactory
    ).apply { toggleGroup = group }

    private fun buildRadioGroup() = ToggleGroup().apply {
        selectedToggleProperty().onValuePresent {
            if (null != it && it is ThemeToggle<*>) {
                val theme = if (Theme.getDefault().javaClass == it.themeClass) Theme.getDefault() else it.themeFactory()
                preferences.editor().put(PreferenceKey.THEME, theme).tryCommit()
                Theme.setDefault(theme)
                logger.debug("Theme selected: {}", theme.javaClass.name)
            }
        }
    }

    private class ThemeToggle<T : Theme>(
        text: String,
        thumbnail: Node,
        val themeClass: Class<T>,
        val themeFactory: () -> T
    ) : RadioButton() {
        init {
            this.text = text
            this.graphic = thumbnail
            this.contentDisplay = ContentDisplay.BOTTOM
            this.isSelected = Theme.getDefault().javaClass == themeClass
            this.padding = Insets(10.0)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ThemeSegmentView::class.java)
    }
}