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
package org.myteer.novel.gui.preloader

import animatefx.animation.BounceIn
import com.jfoenix.controls.JFXProgressBar
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import javafx.scene.image.ImageView
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import org.myteer.novel.main.PropertiesSetup

class PreloaderGUI(private val messageProperty: StringProperty = SimpleStringProperty()) : VBox() {
    companion object {
        private const val MAIN_PANE_STYLE_CLASS = "mainPane"
        private const val LOGO_STYLE_CLASS = "logo"
        private const val APP_NAME_LABEL_STYLE_CLASS = "appNameLabel"
        private const val COMPANY_LABEL_STYLE_CLASS = "companyLabel"
        private const val MESSAGE_LABEL_STYLE_CLASS = "messageLabel"
    }
    private val mainPane: StackPane
    private val center: Node

    init {
        mainPane = buildMainPane()
        center = buildCenter()
        with(mainPane.children) {
            add(center)
            add(buildCompanyLabel())
            add(buildMessageLabel())
            add(buildProgressBar())
        }
        children.add(mainPane)
    }

    fun logoAnimation() {
        BounceIn(center).play()
    }

    private fun buildMainPane(): StackPane {
        return StackPane().apply {
            styleClass.add(MAIN_PANE_STYLE_CLASS)
        }
    }

    private fun buildCenter(): Node {
        return Group(VBox(buildLogo(), buildAppNameLabel()).apply {
            alignment = Pos.CENTER
        })
    }

    private fun buildLogo(): ImageView {
        return ImageView().apply {
            styleClass.add(LOGO_STYLE_CLASS)
        }
    }

    private fun buildAppNameLabel(): Label {
        return Label(System.getProperty(PropertiesSetup.APP_NAME)).apply {
            styleClass.add(APP_NAME_LABEL_STYLE_CLASS)
        }
    }

    private fun buildCompanyLabel(): Label {
        return Label(System.getProperty(PropertiesSetup.APP_COMPANY)).apply {
            styleClass.add(COMPANY_LABEL_STYLE_CLASS)
            StackPane.setAlignment(this, Pos.TOP_RIGHT)
        }
    }

    private fun buildMessageLabel(): Label {
        return Label().apply {
            styleClass.add(MESSAGE_LABEL_STYLE_CLASS)
            textProperty().bind(messageProperty)
            visibleProperty().bind(messageProperty.isNotEmpty)
            StackPane.setAlignment(this, Pos.BOTTOM_LEFT)
            StackPane.setMargin(this, Insets(0.0, 0.0, 15.0, 10.0))
        }
    }

    private fun buildProgressBar(): ProgressBar {
        return JFXProgressBar().apply {
            prefWidthProperty().bind(mainPane.widthProperty())
            StackPane.setAlignment(this, Pos.BOTTOM_CENTER)
        }
    }
}