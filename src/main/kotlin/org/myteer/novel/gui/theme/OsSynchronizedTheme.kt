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

import com.jthemedetecor.OsThemeDetector
import javafx.application.Platform
import javafx.scene.Parent
import javafx.scene.Scene
import org.myteer.novel.i18n.i18n
import java.util.function.Consumer

class OsSynchronizedTheme : Theme() {
    private val osThemeListener: Consumer<Boolean>
    private val osThemeDetector: OsThemeDetector
    private val lightTheme: Theme
    private val darkTheme: Theme
    private var currentTheme: Theme

    init {
        osThemeListener = SyncFunction(this)
        osThemeDetector = OsThemeDetector.getDetector()
        osThemeDetector.registerListener(osThemeListener)
        lightTheme = LightTheme()
        darkTheme = DarkTheme()
        currentTheme = getCurrentTheme()
    }

    private fun getCurrentTheme(): Theme {
        return if (osThemeDetector.isDark) darkTheme else lightTheme
    }

    override fun onThemeDropped() {
        osThemeDetector.removeListener(osThemeListener)
    }

    override fun revoke(scene: Scene) {
        currentTheme.revoke(scene)
    }

    override fun revoke(parent: Parent) {
        currentTheme.revoke(parent)
    }

    override fun apply(scene: Scene) {
        currentTheme.apply(scene)
    }

    override fun apply(parent: Parent) {
        currentTheme.apply(parent)
    }

    private class SyncFunction(private val syncTheme: OsSynchronizedTheme) : Consumer<Boolean> {
        override fun accept(isDark: Boolean) {
            Platform.runLater {
                if (isDark) {
                    syncTheme.currentTheme = syncTheme.darkTheme
                    syncTheme.update(syncTheme.lightTheme)
                } else {
                    syncTheme.currentTheme = syncTheme.lightTheme
                    syncTheme.update(syncTheme.darkTheme)
                }
            }
        }
    }

    companion object {
        fun getMeta() = ThemeMeta(OsSynchronizedTheme::class.java) { i18n("app.ui.theme.sync") }
    }
}