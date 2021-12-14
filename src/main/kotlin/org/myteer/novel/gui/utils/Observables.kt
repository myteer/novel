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

import javafx.beans.binding.Bindings
import javafx.beans.binding.BooleanBinding
import javafx.beans.value.ObservableValue
import javafx.beans.value.ObservableValueBase
import javafx.collections.ObservableList

inline fun <T> constantObservable(crossinline value: () -> T): ObservableValue<T> = object : ObservableValueBase<T>() {
    override fun getValue(): T = value()
}

fun ObservableList<*>.emptyBinding(): BooleanBinding = Bindings.isEmpty(this)

inline fun <T> ObservableValue<T>.onValuePresent(crossinline action: (newValue: T) -> Unit) {
    addListener { _, _, newValue -> action(newValue) }
}