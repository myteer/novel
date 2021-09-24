package org.myteer.novel.gui.api

import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.ButtonType
import javafx.scene.control.Hyperlink
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Region
import javafx.stage.Stage
import javafx.stage.Window
import javafx.util.Duration
import java.util.function.Consumer

interface Context {
    enum class ProgressType {
        NORMAL,
        PAUSED,
        ERROR
    }

    /* Overlays */

    fun showOverlay(region: Region, blocking: Boolean = false)

    fun hideOverlay(region: Region)

    /* Dialogs */

    fun showInformationDialog(title: String, message: String, onResult: Consumer<ButtonType>): ContextDialog

    fun showConfirmationDialog(title: String, message: String, onResult: Consumer<ButtonType>): ContextDialog

    fun showErrorDialog(title: String, message: String, exception: Exception? = null, onResult: Consumer<ButtonType>? = null): ContextDialog

    fun showDialog(title: String, content: Node, onResult: Consumer<ButtonType>, vararg buttonTypes: ButtonType): ContextDialog

    fun showInformationDialogAndWait(title: String, message: String): ButtonType

    fun showConfirmationDialogAndWait(title: String, message: String): ButtonType

    fun showErrorDialogAndWait(title: String, message: String, exception: Exception? = null): ButtonType

    fun showDialogAndWait(title: String, content: Node, vararg buttonTypes: ButtonType): ButtonType

    /* Notifications */

    fun showInformationNotification(title: String, message: String, duration: Duration? = null, onClicked: EventHandler<MouseEvent>? = null, vararg hyperlinks: Hyperlink)

    fun showWarningNotification(title: String, message: String, duration: Duration? = null, onClicked: EventHandler<MouseEvent>? = null)

    fun showErrorNotification(title: String, message: String, duration: Duration? = null, onClicked: EventHandler<MouseEvent>? = null)

    /* Others */

    fun getContextScene(): Scene?

    fun getContextWindow(): Window?

    fun toFrontRequest()

    fun isShowing(): Boolean

    fun showProgress(done: Long, max: Long, type: ProgressType)

    fun showIndeterminateProgress()

    fun stopProgress()

    fun onWindowPresent(action: Consumer<Window>)

    fun close() {
        getContextWindow()?.let {
            if (it is Stage) {
                it.close()
            } else {
                it.hide()
            }
        }
    }

    companion object {
        fun empty() = object : EmptyContext {}
    }
}