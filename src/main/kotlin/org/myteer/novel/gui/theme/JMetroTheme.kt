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
package org.myteer.novel.gui.theme

import javafx.scene.Parent
import javafx.scene.Scene
import jfxtras.styles.jmetro.JMetro
import jfxtras.styles.jmetro.Style
import java.util.stream.Collectors
import java.util.stream.Stream

abstract class JMetroTheme(style: Style) : Theme() {
    companion object {
        private val JMETRO_STYLE_SHEETS: List<String> = Stream.of(
            JMetro::class.java.getDeclaredField("BASE_STYLESHEET_URL"),
            JMetro::class.java.getDeclaredField("BASE_EXTRAS_STYLESHEET_URL"),
            JMetro::class.java.getDeclaredField("BASE_OTHER_LIBRARIES_STYLESHEET_URL"),
            Style::class.java.getDeclaredField("DARK_STYLE_SHEET_URL"),
            Style::class.java.getDeclaredField("LIGHT_STYLE_SHEET_URL")
        )
        .map {
            it.isAccessible = true
            it.get(null).toString()
        }
        .collect(Collectors.toList())
    }

    private val style: Style
    private val additionalStyleSheets: List<String> by lazy { additionalStyleSheets() }

    init {
        this.style = style
    }

    override fun revoke(scene: Scene) {
        scene.stylesheets.removeAll(JMETRO_STYLE_SHEETS)
        scene.stylesheets.removeAll(additionalStyleSheets)
    }

    override fun revoke(parent: Parent) {
        parent.stylesheets.removeAll(JMETRO_STYLE_SHEETS)
        parent.stylesheets.removeAll(additionalStyleSheets)
    }

    override fun apply(scene: Scene) {
        JMetro(style).scene = scene
        scene.stylesheets.addAll(additionalStyleSheets)
    }

    override fun apply(parent: Parent) {
        JMetro(style).parent = parent
        parent.stylesheets.addAll(additionalStyleSheets)
    }

    protected open fun additionalStyleSheets(): List<String> = listOf()
}