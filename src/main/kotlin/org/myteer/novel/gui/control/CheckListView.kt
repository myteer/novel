package org.myteer.novel.gui.control

import javafx.scene.control.cell.CheckBoxListCell

open class CheckListView<T> : org.controlsfx.control.CheckListView<T>() {
    init {
        setCellFactory { CheckBoxListCell<T> { getItemBooleanProperty(it) } }
    }
}