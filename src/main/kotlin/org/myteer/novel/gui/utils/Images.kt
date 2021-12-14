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

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.image.Image
import java.io.BufferedInputStream
import java.util.function.Consumer
import kotlin.reflect.KClass

fun KClass<*>.loadImage(resource: String): Image {
    return Image(this.java.getResourceAsStream(resource)?.let { BufferedInputStream(it) })
}

fun asyncLoadImage(resource: String, onReady: Consumer<Image>) {
    val image = Image(resource, true)
    image.progressProperty().addListener(object : ChangeListener<Number> {
        override fun changed(observable: ObservableValue<out Number>, oldValue: Number, newValue: Number) {
            if (1.0 == newValue && image.isError.not()) {
                onReady.accept(image)
                observable.removeListener(this)
            }
        }
    })
}