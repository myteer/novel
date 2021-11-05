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