package org.myteer.novel.gui.info

import javafx.geometry.Insets
import javafx.scene.Cursor
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.Separator
import javafx.scene.control.Tooltip
import javafx.scene.input.MouseButton
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import jfxtras.styles.jmetro.JMetroStyleClass
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.control.HighlightableLabel
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.i18n.i18n
import org.myteer.novel.main.PropertiesSetup
import org.myteer.novel.utils.SystemBrowser
import java.util.*

class InformationView(val context: Context) : VBox(5.0) {
    companion object {
        private const val GIT_REPO_URL = "https://github.com/myteer/novel"
        private const val LICENSE_URL = "https://github.com/myteer/novel/blob/master/LICENSE"
        private const val LICENSE_NAME = "Apache License 2.0"
    }

    init {
        styleClass.addAll("information-view", JMetroStyleClass.BACKGROUND)
        buildUI()
    }

    private fun buildUI() {
        buildAppInfo()
        children.add(Separator())
        buildLanguageInfo()
        children.add(Separator())
        buildJavaInfo()
        children.add(Separator())
        buildLogInfo()
        children.add(Separator())
        buildBottom()
    }

    private fun buildAppInfo() {
        children.addAll(
            KeyValuePair("info.version", System.getProperty(PropertiesSetup.APP_VERSION)),
            KeyValuePair("info.developer", System.getProperty(PropertiesSetup.APP_DEVELOPER)),
            KeyValuePair("info.license", Label(LICENSE_NAME).apply {
                cursor = Cursor.HAND
                tooltip = Tooltip(LICENSE_URL)
                setOnMouseClicked {
                    if (MouseButton.PRIMARY == it.button) {
                        SystemBrowser.browse(LICENSE_URL)
                    }
                }
            })
        )
    }

    private fun buildLanguageInfo() {
        children.add(KeyValuePair("info.lang", Locale.getDefault().displayLanguage))
    }

    private fun buildJavaInfo() {
        children.addAll(
            KeyValuePair("java.home", System.getProperty("java.home")),
            KeyValuePair("java.vm", System.getProperty("java.vm.name").let { vmName ->
                System.getProperty("java.vm.vendor")?.let {
                    if (it.isNotBlank()) {
                        "$vmName ${i18n("java.vm.by")} $it"
                    } else null
                } ?: vmName
            }),
            KeyValuePair("java.version", System.getProperty("java.version")),
            KeyValuePair("javafx.version", System.getProperty("javafx.version"))
        )
    }

    private fun buildLogInfo() {
        children.add(KeyValuePair("info.logs.loc", System.getProperty(PropertiesSetup.LOG_FILE_PATH_FULL)))
    }

    private fun buildBottom() = StackPane(
        Group(HBox(10.0).also {
            it.children.add(buildGitButton())
            it.children.add(buildDependencyButton())
        })
    ).also { children.add(it) }

    private fun buildGitButton() = Button().apply {
        text = i18n("info.show.github")
        graphic = icon("github-icon")
        setOnAction {
            SystemBrowser.browse(GIT_REPO_URL)
        }
    }

    private fun buildDependencyButton() = Button().apply {
        text = i18n("info.show.dependencies")
        graphic = icon("code-braces-icon")
    }

    private class KeyValuePair(i18n: String, value: Node) : HBox() {
        constructor(i18n: String, value: String?) : this(i18n, HighlightableLabel(value).apply { setHgrow(this, Priority.ALWAYS) })

        init {
            children.add(Label(i18n(i18n)))
            children.add(Label(":").apply { padding = Insets(0.0, 5.0, 0.0, 0.0) })
            children.add(value)
        }
    }
}