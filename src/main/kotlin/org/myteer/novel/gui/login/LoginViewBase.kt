package org.myteer.novel.gui.login

import animatefx.animation.FadeInUp
import javafx.application.Platform
import javafx.beans.value.ObservableStringValue
import javafx.scene.Group
import javafx.scene.input.TransferMode
import javafx.scene.layout.Priority
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import org.myteer.novel.db.DatabaseMeta
import org.myteer.novel.gui.utils.onScenePresent
import java.io.File

class LoginViewBase(private val controller: LoginBox.Controller) : VBox() {
    private val loginBox = LoginBox(controller)

    init {
        styleClass.add("login-form")
        buildUI()
        enableDragSupport()
        playAnimation()
    }

    fun titleProperty(): ObservableStringValue = loginBox.titleProperty()

    private fun buildUI() {
        children.addAll(
            LoginToolBar(controller.context, controller.databaseTracker, controller.preferences),
            StackPane(Group(loginBox)).apply { setVgrow(this, Priority.ALWAYS) }
        )
    }

    private fun enableDragSupport() {
        setOnDragOver {
            if (it.dragboard.hasFiles()) {
                it.acceptTransferModes(*TransferMode.COPY_OR_MOVE)
            }
        }
        setOnDragDropped {
            if (it.dragboard.hasFiles()) {
                it.dragboard.files
                    .filter(File::isFile)
                    .map(::DatabaseMeta)
                    .toList()
                    .apply {
                        forEach(controller.databaseTracker::saveDatabase)
                        lastOrNull()?.let(loginBox::select)
                    }
            }
        }
    }

    private fun playAnimation() {
        onScenePresent {
            Platform.runLater {
                FadeInUp(children[1]).play()
            }
        }
    }
}