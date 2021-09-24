package org.myteer.novel.gui.api

import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.ButtonType
import javafx.scene.control.Hyperlink
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Region
import javafx.stage.Window
import javafx.util.Duration
import java.util.function.Consumer

interface EmptyContext : Context {
    override fun showOverlay(region: Region, blocking: Boolean) {
        TODO("Not yet implemented")
    }

    override fun hideOverlay(region: Region) {
        TODO("Not yet implemented")
    }

    override fun showInformationDialog(title: String, message: String, onResult: Consumer<ButtonType>): ContextDialog {
        TODO("Not yet implemented")
    }

    override fun showConfirmationDialog(title: String, message: String, onResult: Consumer<ButtonType>): ContextDialog {
        TODO("Not yet implemented")
    }

    override fun showErrorDialog(title: String, message: String, exception: Exception?, onResult: Consumer<ButtonType>?): ContextDialog {
        TODO("Not yet implemented")
    }

    override fun showDialog(title: String, content: Node, onResult: Consumer<ButtonType>, vararg buttonTypes: ButtonType): ContextDialog {
        TODO("Not yet implemented")
    }

    override fun showInformationDialogAndWait(title: String, message: String): ButtonType {
        TODO("Not yet implemented")
    }

    override fun showConfirmationDialogAndWait(title: String, message: String): ButtonType {
        TODO("Not yet implemented")
    }

    override fun showErrorDialogAndWait(title: String, message: String, exception: Exception?): ButtonType {
        TODO("Not yet implemented")
    }

    override fun showDialogAndWait(title: String, content: Node, vararg buttonTypes: ButtonType): ButtonType {
        TODO("Not yet implemented")
    }

    override fun showInformationNotification(title: String, message: String, duration: Duration?, onClicked: EventHandler<MouseEvent>?, vararg hyperlinks: Hyperlink) {
        TODO("Not yet implemented")
    }

    override fun showWarningNotification(title: String, message: String, duration: Duration?, onClicked: EventHandler<MouseEvent>?) {
        TODO("Not yet implemented")
    }

    override fun showErrorNotification(title: String, message: String, duration: Duration?, onClicked: EventHandler<MouseEvent>?) {
        TODO("Not yet implemented")
    }

    override fun getContextScene(): Scene? {
        TODO("Not yet implemented")
    }

    override fun getContextWindow(): Window? {
        TODO("Not yet implemented")
    }

    override fun toFrontRequest() {
        TODO("Not yet implemented")
    }

    override fun isShowing(): Boolean {
        TODO("Not yet implemented")
    }

    override fun showProgress(done: Long, max: Long, type: Context.ProgressType) {
        TODO("Not yet implemented")
    }

    override fun showIndeterminateProgress() {
        TODO("Not yet implemented")
    }

    override fun stopProgress() {
        TODO("Not yet implemented")
    }

    override fun onWindowPresent(action: Consumer<Window>) {
        TODO("Not yet implemented")
    }
}