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
package org.myteer.novel.gui.utils

import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import org.myteer.novel.i18n.i18n

object I18NButtonType {
    val APPLY = createButtonType("Dialog.apply.button", ButtonBar.ButtonData.APPLY)

    val OK = createButtonType("Dialog.ok.button", ButtonBar.ButtonData.OK_DONE)

    val CANCEL = createButtonType("Dialog.cancel.button", ButtonBar.ButtonData.CANCEL_CLOSE)

    val CLOSE = createButtonType("Dialog.close.button", ButtonBar.ButtonData.CANCEL_CLOSE)

    val YES = createButtonType("Dialog.yes.button", ButtonBar.ButtonData.YES)

    val NO = createButtonType("Dialog.no.button", ButtonBar.ButtonData.NO)

    val FINISH = createButtonType("Dialog.finish.button", ButtonBar.ButtonData.FINISH)

    val NEXT = createButtonType("Dialog.next.button", ButtonBar.ButtonData.NEXT_FORWARD)

    val PREVIOUS = createButtonType("Dialog.previous.button", ButtonBar.ButtonData.BACK_PREVIOUS)

    val RETRY = createButtonType("Dialog.retry.button", ButtonBar.ButtonData.YES)

    private fun createButtonType(key: String, buttonData: ButtonBar.ButtonData) = ButtonType(i18n(key), buttonData)
}