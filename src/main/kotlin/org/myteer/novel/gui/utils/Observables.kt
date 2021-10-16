package org.myteer.novel.gui.utils

import javafx.beans.value.ObservableValue
import javafx.beans.value.ObservableValueBase

inline fun <T> constantObservable(crossinline value: () -> T): ObservableValue<T> = object : ObservableValueBase<T>() {
    override fun getValue(): T = value()
}