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
package org.myteer.novel.gui.control

import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import org.myteer.novel.gui.utils.selectedItem
import java.awt.Font
import java.awt.GraphicsEnvironment
import java.util.*

class FontPicker : ComboBox<Font>() {
    init {
        items.addAll(AVAILABLE_FONTS)
        buttonCell = FontCell()
        setCellFactory { FontCell() }
    }

    fun selectByFamily(family: String) {
        items.find { it.getFamily(Locale.ENGLISH) == family }?.let { selectedItem = it }
    }

    private class FontCell : ListCell<Font>() {
        override fun updateItem(item: Font?, empty: Boolean) {
            super.updateItem(item, empty)
            if (empty) {
                text = null
                graphic = null
            } else {
                text = null
                graphic = Label(item?.family).apply {
                    style = "-fx-font-family: '${item?.getFamily(Locale.ENGLISH)}' !important;"
                }
            }
        }
    }

    private companion object {
        val AVAILABLE_FONTS = GraphicsEnvironment.getLocalGraphicsEnvironment().allFonts.distinctBy(Font::getFamily)
    }
}