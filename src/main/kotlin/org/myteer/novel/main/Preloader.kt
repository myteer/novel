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
package org.myteer.novel.main

import javafx.beans.property.SimpleStringProperty
import javafx.scene.Scene
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.stage.StageStyle
import org.myteer.novel.gui.preloader.BackingStage
import org.myteer.novel.gui.preloader.PreloaderGUI
import org.myteer.novel.i18n.i18n
import java.text.MessageFormat

class Preloader : javafx.application.Preloader() {
    companion object {
        private const val STYLE_SHEET = "/org/myteer/novel/gui/theme/preloader.css"
    }
    private val messageProperty = SimpleStringProperty()
    private lateinit var backingStage: BackingStage
    private lateinit var contentStage: Stage

    override fun start(primaryStage: Stage?) {
        val gui = PreloaderGUI(messageProperty)

        backingStage = BackingStage()
        contentStage = backingStage.createChild(StageStyle.UNDECORATED).apply {
            scene = Scene(gui, Color.TRANSPARENT).apply {
                stylesheets.add(STYLE_SHEET)
            }
            centerOnScreen()
            setOnShown {
                gui.logoAnimation()
            }
        }

        backingStage.show()
        contentStage.show()
    }

    override fun handleStateChangeNotification(info: StateChangeNotification?) {
        info?.let {
            if (it.type == StateChangeNotification.Type.BEFORE_START) {
                backingStage.close()
                stop()
            }
        }
    }

    override fun handleApplicationNotification(info: PreloaderNotification?) {
        info?.let {
            when (it) {
                is MessageNotification -> messageProperty.set(it.message)
                is ShowNotification -> contentStage.show()
                is HideNotification -> contentStage.hide()
            }
        }
    }

    class ShowNotification : PreloaderNotification

    class HideNotification : PreloaderNotification

    class MessageNotification(value: String, i18n: Boolean = true, vararg args: String) : PreloaderNotification {
        val message: String

        init {
            message = if (i18n) {
                i18n(value, *args)
            } else {
                if (args.isEmpty()) {
                    value
                } else {
                    MessageFormat.format(value, args)
                }
            }
        }
    }
}