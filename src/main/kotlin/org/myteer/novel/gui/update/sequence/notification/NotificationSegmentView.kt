package org.myteer.novel.gui.update.sequence.notification

import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.scene.Group
import javafx.scene.control.Label
import javafx.scene.control.Separator
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import org.myteer.novel.i18n.i18n
import org.myteer.novel.main.PropertiesSetup
import org.myteer.novel.update.Release

class NotificationSegmentView(private val release: Release) : VBox() {
    init {
        padding = Insets(10.0)
        spacing = 10.0
        buildUI()
    }

    private fun buildUI() {
        children.add(buildHeader())
        children.add(VersionArea(release))
    }

    private fun buildHeader() = StackPane().apply {
        setVgrow(this, Priority.SOMETIMES)
        children.add(Label().apply {
            styleClass.add("update-view-available-label")
            text = i18n("update.available")
            padding = Insets(10.0)
        })
    }

    private class VersionArea(private val release: Release) : StackPane() {
        init {
            buildUI()
        }

        private fun buildUI() {
            children.add(Group(buildHBox()))
        }

        private fun buildHBox() = HBox().apply {
            spacing = 10.0
            children.addAll(
                buildDescriptionLabel("update.dialog.current_version"),
                buildCurrentVersionLabel(),
                Separator(Orientation.VERTICAL),
                buildDescriptionLabel("update.dialog.next_version"),
                buildNextVersionLabel()
            )
        }

        private fun buildDescriptionLabel(i18n: String) = Label().apply {
            text = i18n(i18n)
            HBox.setHgrow(this, Priority.ALWAYS)
        }

        private fun buildCurrentVersionLabel() = Label().apply {
            text = System.getProperty(PropertiesSetup.APP_VERSION)
            HBox.setHgrow(this, Priority.SOMETIMES)
        }

        private fun buildNextVersionLabel() = Label().apply {
            text = release.version
            HBox.setHgrow(this, Priority.SOMETIMES)
        }
    }
}