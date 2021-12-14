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
package org.myteer.novel.gui.dbcreator

import com.jfilegoodies.FileGoodies
import javafx.beans.property.*
import javafx.geometry.Insets
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import javafx.scene.layout.StackPane
import javafx.stage.DirectoryChooser
import org.myteer.novel.config.login.Credentials
import org.myteer.novel.db.DatabaseMeta
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.entry.DatabaseTracker
import org.myteer.novel.gui.utils.SpaceValidator
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.gui.utils.stage
import org.myteer.novel.gui.utils.window
import org.myteer.novel.i18n.i18n
import org.myteer.novel.main.PropertiesSetup
import java.io.File

class DatabaseCreatorForm(
    private val context: Context,
    private val databaseTracker: DatabaseTracker
) : BorderPane() {
    val createdDatabase: ObjectProperty<DatabaseMeta> = SimpleObjectProperty()

    private val databaseName: StringProperty = SimpleStringProperty()
    private val databaseDir: StringProperty = SimpleStringProperty()
    private val authentication: BooleanProperty = SimpleBooleanProperty(true)
    private val username: StringProperty = SimpleStringProperty()
    private val password: StringProperty = SimpleStringProperty()
    private val passwordRepeat: StringProperty = SimpleStringProperty()

    private val fullPath: StringProperty = SimpleStringProperty().apply {
        bind(
            databaseDir.concat(File.separator)
                .concat(databaseName)
                .concat(".")
                .concat(System.getProperty(PropertiesSetup.APP_FILE_EXTENSION))
        )
    }

    private val directoryChooser = DirectoryChooser()

    init {
        buildUI()
    }

    private fun buildUI() {
        center = buildCenter()
        bottom = buildBottom()
    }

    private fun buildCenter() = ScrollPane(buildGrid()).apply {
        isFitToWidth = true
    }

    private fun buildBottom() = StackPane(buildCreateButton()).apply {
        padding = Insets(10.0)
    }

    private fun openDirectory() {
        directoryChooser.showDialog(this.window)?.let {
            databaseDir.set(it.absolutePath)
        }
    }

    private fun buildCreateButton() = Button().apply {
        maxWidth = Double.MAX_VALUE
        minHeight = 35.0
        text = i18n("database.creator.create")
        isDefaultButton = true
        setOnAction { create() }
    }

    private fun buildGrid() = object : GridPane() {
        init {
            padding = Insets(10.0)
            hgap = 5.0
            vgap = 5.0
            buildUI()
        }

        private fun buildUI() {
            children.addAll(
                buildLabel("database.creator.db_name", 0, 0),
                buildNameField(),
                buildLabel("database.creator.db_dir", 1, 0),
                buildDirField(),
                buildDirOpenButton(),
                buildLabel("database.creator.full_path", 0, 2),
                buildFullPathField(),
                buildAuthenticationCheckBox(),
                buildUsernameField(),
                buildPasswordField(),
                buildPasswordRepeatField()
            )
        }

        private fun buildLabel(i18n: String, column: Int, row: Int) = Label(i18n(i18n)).apply {
            setConstraints(this, column, row)
        }

        private fun buildNameField() = TextField().apply {
            setConstraints(this, 0, 1)
            setHgrow(this, Priority.SOMETIMES)
            minHeight = 35.0
            databaseName.bind(textProperty())
            textFormatter = SpaceValidator()
        }

        private fun buildDirField() = TextField().apply {
            setConstraints(this, 1, 1)
            setHgrow(this, Priority.ALWAYS)
            minHeight = 35.0
            text = System.getProperty(PropertiesSetup.DEFAULT_DIRECTORY_PATH)
            databaseDir.bindBidirectional(textProperty())
        }

        private fun buildDirOpenButton() = Button().apply {
            setConstraints(this, 2, 1)
            contentDisplay = ContentDisplay.GRAPHIC_ONLY
            graphic = icon("folder-open-icon")
            minHeight = 35.0
            tooltip = Tooltip(i18n("data.source.adder.choose.dir"))
            setOnAction { openDirectory() }
        }

        private fun buildFullPathField() = TextField().apply {
            setConstraints(this, 0, 3)
            setColumnSpan(this, 3)
            minHeight = 35.0
            isEditable = false
            textProperty().bind(fullPath)
        }

        private fun buildAuthenticationCheckBox() = CheckBox(i18n("database.creator.db_auth")).apply {
            setConstraints(this, 0, 4)
            setColumnSpan(this, 3)
            setHgrow(this, Priority.SOMETIMES)
            selectedProperty().bindBidirectional(authentication)
        }

        private fun buildUsernameField() = TextField().apply {
            setMargin(this, Insets(5.0, 0.0, 0.0, 0.0))
            setConstraints(this, 0, 5)
            setColumnSpan(this, 3)
            minHeight = 35.0
            promptText = i18n("credentials.username")
            visibleProperty().bind(authentication)
            managedProperty().bind(authentication)
            username.bind(textProperty())
            textFormatter = SpaceValidator()
        }

        private fun buildPasswordField() = PasswordField().apply {
            setConstraints(this, 0, 6)
            setColumnSpan(this, 3)
            minHeight = 35.0
            promptText = i18n("credentials.password")
            visibleProperty().bind(authentication)
            managedProperty().bind(authentication)
            password.bind(textProperty())
            textFormatter = SpaceValidator()
        }

        private fun buildPasswordRepeatField() = PasswordField().apply {
            setConstraints(this, 0, 7)
            setColumnSpan(this, 3)
            minHeight = 35.0
            promptText = i18n("database.creator.password.repeat")
            visibleProperty().bind(authentication)
            managedProperty().bind(authentication)
            passwordRepeat.bind(textProperty())
            textFormatter = SpaceValidator()
        }
    }

    private fun create() {
        validateInputs { databaseMeta, credentials ->
            createdDatabase.set(databaseMeta)
            NitriteDatabase.builder()
                .databaseMeta(databaseMeta)
                .onFailed { message, t ->
                    createdDatabase.set(null)
                    context.showErrorDialog(i18n("database.create_failed"), message, t as Exception?)
                }.touch(credentials)
            createdDatabase.get()?.let {
                databaseTracker.saveDatabase(it)
            }
            this.stage?.close()
        }
    }

    private fun validateInputs(onSuccess: (DatabaseMeta, Credentials) -> Unit) {
        val databaseDirFile = databaseDir.get().let(::File)
        val databaseFile = fullPath.get().let(::File)
        when {
            databaseName.get()?.isBlank() ?: true -> {
                showErrorDialog(
                    "database.creator.missing_name.title",
                    "database.creator.missing_name.message"
                )
                false
            }
            databaseDir.get()?.isBlank() ?: true -> {
                showErrorDialog(
                    "database.creator.missing_dir.title",
                    "database.creator.missing_dir.message"
                )
                false
            }
            FileGoodies.hasNotValidPath(databaseDirFile) -> {
                showErrorDialog(
                    "database.creator.invalid_dir.title",
                    "database.creator.invalid_dir.message",
                    databaseDirFile
                )
                false
            }
            databaseFile.exists() -> {
                showErrorDialog(
                    "database.creator.file_already_exists.title",
                    "database.creator.file_already_exists.message",
                    FileGoodies.shortenedFilePath(databaseFile, 1)
                )
                false
            }
            authentication.get() && username.get()?.isBlank() ?: true -> {
                showErrorDialog(
                    "database.creator.empty_username.title",
                    "database.creator.empty_username.message"
                )
                false
            }
            authentication.get() && password.get()?.isBlank() ?: true -> {
                showErrorDialog(
                    "database.creator.empty_password.title",
                    "database.creator.empty_password.message"
                )
                false
            }
            authentication.get() && password.get() != passwordRepeat.get() -> {
                showErrorDialog(
                    "database.creator.passwords_not_match.title",
                    "database.creator.passwords_not_match.message"
                )
                false
            }
            databaseDirFile.exists().not() -> {
                showInfoDialog(
                    "database.creator.dir_not_exist.title",
                    "database.creator.dir_not_exist.message",
                    databaseDirFile.name
                )
                databaseDirFile.mkdirs()
                true
            }
            else -> true
        }.takeIf { it }?.let {
            onSuccess(
                DatabaseMeta(databaseName.get(), databaseFile),
                Credentials(username.get(), password.get())
            )
        }
    }

    private fun showInfoDialog(title: String, message: String, vararg args: Any): ButtonType {
        return context.showInformationDialogAndWait(i18n(title, *args), i18n(message))
    }

    private fun showErrorDialog(title: String, message: String, vararg args: Any): ButtonType {
        return context.showErrorDialogAndWait(i18n(title), i18n(message, *args))
    }
}