package org.myteer.novel.gui.login

import javafx.beans.property.*
import javafx.beans.value.ObservableStringValue
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.control.*
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import org.myteer.novel.config.Preferences
import org.myteer.novel.config.login.Credentials
import org.myteer.novel.config.login.LoginData
import org.myteer.novel.db.DatabaseMeta
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.entry.DatabaseTracker
import org.myteer.novel.gui.utils.SpaceValidator
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.gui.utils.refresh
import org.myteer.novel.i18n.i18n
import org.myteer.novel.main.PropertiesSetup

class LoginBox(private val controller: Controller) : VBox(10.0) {
    private val itemSelected: BooleanProperty = SimpleBooleanProperty()
    private val usernameInput: StringProperty = SimpleStringProperty()
    private val passwordInput: StringProperty = SimpleStringProperty()
    private val remember: BooleanProperty = SimpleBooleanProperty()
    private val databaseChooser: ObjectProperty<ComboBox<DatabaseMeta>> = SimpleObjectProperty()

    var selectedItem: DatabaseMeta?
        get() = databaseChooser.get().selectionModel.selectedItem
        set(value) {
            select(value)
        }

    init {
        styleClass.add("login-box")
        maxWidth = 650.0
        height = 550.0
        buildUI()
        controller.loginBox = this
    }

    fun titleProperty(): ObservableStringValue = databaseChooser.get().selectionModel.selectedItemProperty().asString()

    fun refresh() {
        databaseChooser.get().refresh()
    }

    fun addItem(item: DatabaseMeta) {
        databaseChooser.get().items.add(item)
    }

    fun removeItem(item: DatabaseMeta) {
        databaseChooser.get().items.remove(item)
    }

    fun select(databaseMeta: DatabaseMeta?) {
        databaseChooser.get().selectionModel.select(databaseMeta)
    }

    fun addSelectedItemListener(listener: (DatabaseMeta?) -> Unit) {
        databaseChooser.get().selectionModel.selectedItemProperty().addListener { _, _, it -> listener(it) }
    }

    fun fillForm(loginData: LoginData) {
        databaseChooser.get().let { databaseChooser ->
            databaseChooser.items.addAll(loginData.getSavedDatabases())
            loginData.getSelectedDatabase()?.let(databaseChooser.selectionModel::select)
            loginData.getAutoLoginDatabase()?.let {
                remember.set(true)
                loginData.getAutoLoginCredentials().run {
                    usernameInput.set(username)
                    passwordInput.set(password)
                }
            }
        }
    }

    private fun buildUI() {
        children.addAll(
            buildHeader(),
            buildDatabaseChooserArea(),
            buildForm(),
            Separator(),
            buildDatabaseCreateButton()
        )
    }

    private fun buildHeader() = StackPane().apply {
        styleClass.add("header")
        padding = Insets(20.0)
        HBox(10.0).run {
            children.add(ImageView().apply { styleClass.add("logo") })
            children.add(StackPane(Label(System.getProperty(PropertiesSetup.APP_NAME)).apply { styleClass.add("label") }))
            Group(this)
        }.let(children::add)
    }

    private fun buildDatabaseChooserArea() = HBox(5.0).apply {
        children.addAll(
            buildComboBox(),
            buildFileChooserButton(),
            buildDatabaseManagerButton()
        )
        setMargin(this, Insets(0.0, 20.0, 0.0, 20.0))
    }

    private fun buildComboBox() = ComboBox<DatabaseMeta>().apply {
        minHeight = 35.0
        minWidth = 355.0
        maxWidth = Double.MAX_VALUE
        promptText = i18n("login.source.combo.prompt")
        itemSelected.bind(selectionModel.selectedItemProperty().isNotNull)
        buttonCell = ComboBoxButtonCell(controller.databaseTracker)
        setCellFactory { DatabaseChooserItem(controller.databaseTracker) }
        HBox.setHgrow(this, Priority.ALWAYS)
        databaseChooser.set(this)
    }

    private fun buildFileChooserButton() = Button().apply {
        tooltip = Tooltip(i18n("login.source.open"))
        graphic = icon("folder-open-icon")
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
        minHeight = 35.0
        minWidth = 40.0
        setOnAction {
            controller.openFile()
        }
    }

    private fun buildDatabaseManagerButton() = Button().apply {
        tooltip = Tooltip(i18n("login.db.manager.open"))
        graphic = icon("database-manager-icon")
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
        minHeight = 35.0
        minWidth = 40.0
        setOnAction {
            controller.openDatabaseManager()
        }
    }

    private fun buildForm() = VBox(10.0).apply {
        children.addAll(
            Separator(),
            buildUsernameInput(),
            buildPasswordInput(),
            buildRememberCheckBox(),
            buildLoginButton()
        )
        visibleProperty().bind(itemSelected)
        managedProperty().bind(itemSelected)
        setMargin(this, Insets(0.0, 20.0, 20.0, 20.0))
    }

    private fun buildUsernameInput() = TextField().apply {
        minHeight = 35.0
        prefColumnCount = 10
        promptText = i18n("credentials.username")
        textFormatter = SpaceValidator()
        usernameInput.bindBidirectional(textProperty())
    }

    private fun buildPasswordInput() = PasswordField().apply {
        minHeight = 35.0
        prefColumnCount = 10
        promptText = i18n("credentials.password")
        textFormatter = SpaceValidator()
        passwordInput.bindBidirectional(textProperty())
    }

    private fun buildRememberCheckBox() = CheckBox().apply {
        alignment = Pos.CENTER_RIGHT
        text = i18n("login.form.remember")
        remember.bindBidirectional(selectedProperty())
    }

    private fun buildLoginButton() = Button().apply {
        minHeight = 35.0
        maxWidth = Double.MAX_VALUE
        text = i18n("login.form.login")
        isDefaultButton = true
        setOnAction {
            databaseChooser.get().selectionModel.selectedItem?.let {
                controller.login(
                    it,
                    Credentials(
                        usernameInput.get() ?: "",
                        passwordInput.get() ?: ""
                    ),
                    remember.get()
                )
            }
        }
    }

    private fun buildDatabaseCreateButton() = Button().apply {
        styleClass.add("source-adder")
        minHeight = 35.0
        maxWidth = Double.MAX_VALUE
        text = i18n("login.add.source")
        graphic = icon("database-plus-icon")
        setOnAction {
            controller.openDatabaseCreator()
        }
    }

    private class ComboBoxButtonCell(databaseTracker: DatabaseTracker) : DatabaseChooserItem(databaseTracker) {
        override fun updateItem(item: DatabaseMeta?, empty: Boolean) {
            super.updateItem(item, empty)
            if (null == item) {
                text = i18n("login.source.combo.prompt")
            }
        }
    }

    private open class DatabaseChooserItem(private val databaseTracker: DatabaseTracker) : ListCell<DatabaseMeta?>() {
        companion object {
            private const val NOT_EXISTS_CLASS = "state-indicator-file-not-exists"
            private const val USED_CLASS = "state-indicator-used"
        }

        init {
            maxWidth = 650.0
        }

        override fun updateItem(item: DatabaseMeta?, empty: Boolean) {
            super.updateItem(item, empty)
            when {
                null == item || empty -> {
                    text = null
                    graphic = null
                }
                else -> {
                    text = item.toString()
                    val dbFile = item.file!!
                    when {
                        dbFile.exists().not() || dbFile.isDirectory -> {
                            tooltip = Tooltip(i18n("file.not.exists"))
                            graphic = icon("warning-icon").apply { styleClass.add(NOT_EXISTS_CLASS) }
                        }
                        databaseTracker.isDatabaseUsed(item) -> {
                            tooltip = Tooltip(i18n("database.currently.used"))
                            graphic = icon("play-icon").apply { styleClass.add(USED_CLASS) }
                        }
                        else -> {
                            tooltip = null
                            graphic = null
                        }
                    }
                }
            }
        }
    }

    interface Controller {
        var loginBox: LoginBox?

        val context: Context
        val preferences: Preferences
        val databaseTracker: DatabaseTracker
        val loginData: LoginData

        fun openDatabaseManager()
        fun openFile()
        fun openDatabaseCreator()
        fun login(databaseMeta: DatabaseMeta, credentials: Credentials, remember: Boolean)
    }
}