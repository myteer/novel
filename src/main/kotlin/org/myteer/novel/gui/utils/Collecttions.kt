package org.myteer.novel.gui.utils

import javafx.collections.FXCollections
import javafx.collections.ObservableList

fun <T> ObservableList<T>.copy(): ObservableList<T> = FXCollections.observableArrayList(java.util.List.copyOf(this))