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
package org.myteer.novel.gui.update.sequence.download

import com.jfilegoodies.FileGoodies
import com.juserdirs.UserDirectories
import javafx.beans.binding.Bindings
import javafx.beans.binding.ObjectBinding
import javafx.beans.property.*
import javafx.concurrent.Service
import javafx.concurrent.Task
import javafx.css.PseudoClass
import javafx.geometry.Insets
import javafx.scene.Cursor
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.DirectoryChooser
import org.controlsfx.control.textfield.CustomTextField
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.control.WebsiteHyperlink
import org.myteer.novel.gui.update.UpdateDialog
import org.myteer.novel.gui.utils.*
import org.myteer.novel.i18n.i18n
import org.myteer.novel.update.Release
import org.myteer.novel.update.ReleaseAsset
import org.slf4j.LoggerFactory
import java.io.File

class DownloadView(private val context: Context, private val release: Release) : VBox() {
    private val selectedAsset = SimpleObjectProperty<ReleaseAsset>()
    private val downloadDirectory = SimpleStringProperty()
    private val downloadService = DownloadService(context, selectedAsset, downloadDirectory)
    private val dialog: UpdateDialog get() = parent.parent as UpdateDialog
    private val downloadFile: File get() = File(downloadDirectory.get(), selectedAsset.get()?.name ?: "")

    init {
        padding = Insets(10.0)
        spacing = 10.0
        prefWidth = 400.0
        buildUI()
    }

    private fun buildUI() {
        children.addAll(
            Label(i18n("update.dialog.download.select_format")),
            buildAssetChooser(),
            Separator(),
            Label(i18n("update.dialog.download.path.prompt")),
            buildPathField(),
            buildDownloadArea(),
            buildProgressBar(),
            buildProgressDetailArea(),
            buildInstallButton(),
            buildOpenButton(),
            buildWebsiteHyperlink()
        )
    }

    private fun buildAssetChooser() = AssetChooser(release.assets!!).apply {
        disableProperty().bind(downloadService.runningProperty())
        this@DownloadView.selectedAsset.bind(selectedAsset)
    }

    private fun buildPathField() = CustomTextField().also { textField ->
        textField.disableProperty().bind(downloadService.runningProperty())
        textField.text = UserDirectories.get().downloadsDirectoryPath()
        textField.isEditable = false
        textField.right = icon("folder-open-icon").apply {
            cursor = Cursor.HAND
            setOnMouseClicked {
                textField.text = DirectoryChooser().apply { initialDirectory = textField.text?.let(::File) }
                    .showDialog(context.getContextWindow())
                    ?.absolutePath
                    ?: textField.text
            }
        }.let { StackPane(it).apply { padding = Insets(5.0) } }
        downloadDirectory.bind(textField.textProperty())
    }

    private fun buildDownloadArea() = HBox(5.0, buildDownloadButton(), buildPauseButton(), buildSuspendButton())

    private fun buildDownloadButton() = Button().apply {
        text = i18n("update.dialog.download_binary")
        maxWidth = Double.MAX_VALUE
        HBox.setHgrow(this, Priority.ALWAYS)
        disableProperty().bind(
            downloadService.runningProperty()
                .or(downloadDirectory.isNull)
                .or(selectedAsset.isNull)
                .or(downloadService.valueProperty().isNotNull)
        )
        setOnAction {
            initUpdateDialogBehaviour()
            checkDownloadFileExists {
                downloadService.reset()
                downloadService.start()
            }
        }
    }

    private fun buildPauseButton() = Button().apply {
        disableProperty().bind(downloadService.runningProperty().not())
        graphicProperty().bind(
            Bindings.createObjectBinding(
                { icon(if (downloadService.isPaused) "play-icon" else "pause-icon") },
                downloadService.runningProperty(),
                downloadService.pausedProperty
            )
        )
        setOnAction {
            downloadService.pauseOrResume()
        }
    }

    private fun buildSuspendButton() = Button().apply {
        disableProperty().bind(downloadService.runningProperty().not())
        graphic = icon("stop-icon")
        setOnAction {
            downloadService.cancel()
        }
    }

    private fun buildProgressBar() = ProgressBar().apply {
        bindFullVisibilityTo(downloadService.runningProperty())
        progressProperty().bind(downloadService.progressProperty())
        downloadService.pausedProperty.onValuePresent {
            pseudoClassStateChanged(PseudoClass.getPseudoClass("paused"), it)
        }
    }

    private fun buildProgressDetailArea() = HBox(2.0, buildProgressMessageLabel(), buildProgressTitleLabel())

    private fun buildProgressMessageLabel() = Label().apply {
        bindFullVisibilityTo(downloadService.runningProperty())
        textProperty().bind(downloadService.messageProperty())
    }

    private fun buildProgressTitleLabel() = Label().apply {
        bindFullVisibilityTo(downloadService.runningProperty())
        textProperty().bind(downloadService.titleProperty())
    }

    private fun buildInstallButton() = Button().apply {
        bindFullVisibilityTo(downloadService.valueProperty().isNotNull)
        text = i18n("update.dialog.download.install")
        isDefaultButton = true
        maxWidth = Double.MAX_VALUE
        setOnAction {
            downloadService.value?.let {
                it.takeIf(FileGoodies::isOSExecutable)?.let { file ->
                    try {
                        Runtime.getRuntime().exec(file.absolutePath)
                    } catch (e: Exception) {
                        context.showErrorDialog(
                            i18n("update.dialog.download.install.failed.title"),
                            i18n("update.dialog.download.install.failed.message", file.name),
                            e
                        )
                    }
                } ?: it.revealInExplorer()
            }
        }
    }

    private fun buildOpenButton() = Button().apply {
        bindFullVisibilityTo(downloadService.valueProperty().isNotNull)
        text = i18n("update.dialog.download.open_in_explorer")
        maxWidth = Double.MAX_VALUE
        setOnAction {
            downloadService.value?.revealInExplorer()
        }
    }

    private fun buildWebsiteHyperlink() = WebsiteHyperlink(i18n("website.open"), release.website).asCentered()

    private fun initUpdateDialogBehaviour() {
        dialog.prevButtonDisableProperty().bind(downloadService.runningProperty())
        dialog.nextButtonDisableProperty().bind(downloadService.runningProperty())
    }

    private inline fun checkDownloadFileExists(crossinline action: () -> Unit) {
        downloadFile.takeUnless(File::exists)?.let { action() }
            ?: context.showConfirmationDialog(
                i18n("update.dialog.download.install.file_exists.title", downloadFile.name),
                i18n("update.dialog.download.install.file_exists.message")
            ) { if (it.typeEquals(ButtonType.YES)) action() }
    }

    private class AssetChooser(private val assets: List<ReleaseAsset>) : VBox() {
        private val radioGroup = ToggleGroup()
        val selectedAsset: ObjectBinding<ReleaseAsset?> = Bindings.createObjectBinding(
            { radioGroup.selectedAsset },
            radioGroup.selectedToggleProperty()
        )

        init {
            spacing = 10.0
            children.add(buildScrollPane())
            children.add(buildSizeIndicatorLabel())
        }

        private fun buildScrollPane() = object : ScrollPane() {
            init {
                padding = Insets(5.0)
                isFitToWidth = true
                maxHeight = 120.0
                content = buildRadioBoxes().takeIf(List<*>::isNotEmpty)?.let {
                    VBox(5.0).apply { children.addAll(it) }
                } ?: NoBinaryAvailablePlaceHolder()
            }

            private fun buildRadioBoxes() = assets.sortedBy(ReleaseAsset::name).map {
                RadioButton(it.name).apply {
                    properties["release.asset"] = it
                    toggleGroup = radioGroup
                }
            }
        }

        private fun buildSizeIndicatorLabel() = Label().apply {
            textProperty().bind(
                SimpleStringProperty(i18n("update.dialog.download.size"))
                    .concat(" ")
                    .concat(
                        Bindings.createStringBinding({
                            radioGroup.selectedAsset?.sizeInMB?.toString()?.plus(" MB") ?: "-"
                        }, radioGroup.selectedToggleProperty())
                    )
            )
        }

        private val Toggle.asset get() = properties["release.asset"] as? ReleaseAsset
        private val ToggleGroup.selectedAsset get() = selectedToggle?.asset
    }

    private class DownloadService(
        private val context: Context,
        private val releaseAsset: ObjectProperty<ReleaseAsset>,
        private val downloadDirectory: StringProperty
    ) : Service<File?>() {
        val pausedProperty = SimpleBooleanProperty()
        val isPaused: Boolean get() = pausedProperty.get()
        private var task: DownloadTask? = null

        init {
            setOnFailed { onFailed(it.source.exception) }
            setOnSucceeded { onSucceeded() }
            setOnCancelled { onCancelled() }
            progressProperty().onValuePresent { onProgress() }
        }

        override fun createTask(): Task<File?> = DownloadTask(releaseAsset.get(), File(downloadDirectory.get())).also {
            pausedProperty.bind(it.pausedProperty().and(runningProperty()))
            task = it
        }

        fun pauseOrResume() {
            task?.let { it.setPaused(!it.isPaused()) }
        }

        private fun onFailed(exception: Throwable) {
            logger.error("Failed during update downloading", exception)
            context.showProgress(workDone.toLong(), totalWork.toLong(), Context.ProgressType.ERROR)
            context.showErrorDialog(
                i18n("update.dialog.download.failed.title"),
                i18n("update.dialog.download.failed.message"),
                exception as Exception?
            ) { context.stopProgress() }
        }

        private fun onSucceeded() {
            context.stopProgress()
            context.getContextWindow()?.requestFocus()
        }

        private fun onCancelled() {
            context.stopProgress()
        }

        private fun onProgress() {
            context.showProgress(
                workDone.toLong(),
                totalWork.toLong(),
                when {
                    isPaused -> Context.ProgressType.PAUSED
                    else -> Context.ProgressType.NORMAL
                }
            )
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DownloadView::class.java)
    }
}