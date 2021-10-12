package org.myteer.novel.gui.login.quick

import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import org.myteer.novel.config.login.Credentials
import org.myteer.novel.db.DatabaseMeta
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.login.DatabaseLoginListener
import org.myteer.novel.gui.utils.SpaceValidator
import org.myteer.novel.i18n.i18n
import org.slf4j.LoggerFactory

class QuickLoginForm(
    private val context: Context,
    private val databaseMeta: DatabaseMeta,
    private val databaseLoginListener: DatabaseLoginListener
) : VBox(10.0) {
    private val usernameInput: StringProperty = SimpleStringProperty()
    private val passwordInput: StringProperty = SimpleStringProperty()

    init {
        setMargin(this, Insets(10.0))
        setVgrow(this, Priority.ALWAYS)
        buildUI()
    }

    private fun buildUI() {
        children.add(buildUsernameInputField())
        children.add(buildPasswordInputField())
        children.add(buildLoginButton())
    }

    private fun buildUsernameInputField() = TextField().apply {
        minHeight = 35.0
        prefColumnCount = 10
        promptText = i18n("credentials.username")
        textFormatter = SpaceValidator()
        usernameInput.bindBidirectional(textProperty())
    }

    private fun buildPasswordInputField() = PasswordField().apply {
        minHeight = 35.0
        prefColumnCount = 10
        promptText = i18n("credentials.password")
        textFormatter = SpaceValidator()
        passwordInput.bindBidirectional(textProperty())
    }

    private fun buildLoginButton() = Button().apply {
        minHeight = 35.0
        maxWidth = Double.MAX_VALUE
        text = i18n("login.form.login")
        isDefaultButton = true
        setOnAction {
            login()
        }
    }

    private fun login() {
        Credentials(
            usernameInput.get() ?: "",
            passwordInput.get() ?: ""
        ).let { credentials ->
            NitriteDatabase.builder()
                .databaseMeta(databaseMeta)
                .onFailed { message, t ->
                    context.showErrorDialog(i18n("login.failed"), message, t as Exception?)
                    logger.error("Failed to create/open the database", t)
                }
                .build(credentials)?.let {
                    logger.debug("Quick login was successful, closing the QuickLoginWindow")
                    databaseLoginListener.onDatabaseOpened(it)
                    context.close()
                }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(QuickLoginForm::class.java)
    }
}