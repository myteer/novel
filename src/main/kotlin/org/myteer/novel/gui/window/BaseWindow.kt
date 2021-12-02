package org.myteer.novel.gui.window

import de.jangassen.MenuToolkit
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.StringProperty
import javafx.event.EventHandler
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.ButtonType
import javafx.scene.control.MenuBar
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import javafx.stage.WindowEvent
import org.myteer.novel.config.PreferenceKey
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.theme.Theme
import org.myteer.novel.gui.theme.Themeable
import org.myteer.novel.gui.utils.loadImage
import org.myteer.novel.gui.utils.typeEquals
import org.myteer.novel.i18n.i18n
import org.myteer.novel.utils.os.OsInfo
import org.slf4j.LoggerFactory

open class BaseWindow<C> : Stage, Themeable where C : Parent, C : Context {
    private var content: C? = null
    protected var showExitDialog: Boolean = false

    init {
        setupIconPack()
        buildWindowCloseEventHandler()
        buildFullScreenExitHint()
        opacityProperty().bind(globalOpacity)
        addEventHandler(WindowEvent.WINDOW_SHOWING) {
            Theme.registerThemeable(this)
        }
    }

    constructor() : super()

    private constructor(title: String) : this() {
        this.title = title
    }

    protected constructor(title: String, content: C) : this(title) {
        this.content = content
        this.scene = Scene(content)
    }

    protected constructor(title: String, menuBar: MenuBar, content: C) : this(title) {
        this.content = content
        this.scene = Scene(buildMenuBarContent(menuBar, content))
    }

    protected constructor(titleProperty: StringProperty, content: C) {
        this.content = content
        this.scene = Scene(content)
        this.titleProperty().bind(titleProperty)
    }

    private fun buildMenuBarContent(menuBar: MenuBar, content: C): Parent =
        when {
            OsInfo.isMac() -> content.also {
                logger.debug("MacOS detected: building native menu-bar...")
                MenuToolkit.toolkit().setMenuBar(this, menuBar)
            }
            else -> {
                logger.debug("MacOS is not detected: building JavaFX based menu-bar...")
                BorderPane(content).apply { top = menuBar }
            }
        }

    fun makeFocused() {
        isIconified = false
        toFront()
    }

    private fun setupIconPack() {
        icons.addAll(
            BaseWindow::class.loadImage(LOGO_16),
            BaseWindow::class.loadImage(LOGO_32),
            BaseWindow::class.loadImage(LOGO_64),
            BaseWindow::class.loadImage(LOGO_128),
            BaseWindow::class.loadImage(LOGO_256),
            BaseWindow::class.loadImage(LOGO_512)
        )
    }

    private fun buildWindowCloseEventHandler() {
        addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, WindowCloseRequestHandler())
    }

    private fun buildFullScreenExitHint() {
        fullScreenExitHint = i18n("window.fullscreen.exit.hint")
    }

    override fun handleThemeApply(newTheme: Theme, oldTheme: Theme) {
        scene?.root?.let {
            oldTheme.revoke(it)
            newTheme.apply(it)
        }
    }

    private inner class WindowCloseRequestHandler : EventHandler<WindowEvent> {
        private var dialogShowing: Boolean = false

        override fun handle(event: WindowEvent) {
            this@BaseWindow.content?.also { context ->
                if (this@BaseWindow.showExitDialog) {
                    this@BaseWindow.makeFocused()
                    when {
                        dialogShowing.not() -> {
                            dialogShowing = true
                            val buttonType = context.showConfirmationDialogAndWait(
                                i18n("window.close.dialog.title"),
                                i18n("window.close.dialog.message")
                            )
                            dialogShowing = false
                            if (ButtonType.NO.typeEquals(buttonType)) {
                                event.consume()
                            }
                        }
                        else -> event.consume()
                    }
                }
            }
        }
    }

    companion object {
        val GLOBAL_OPACITY_CONFIG_KEY = PreferenceKey(
            "basewindow.global.opacity",
            Double::class.java,
            { 1.0 }
        )
        val globalOpacity: DoubleProperty = SimpleDoubleProperty(1.0)

        private const val LOGO_16 = "/org/myteer/novel/image/logo/book_16.png"
        private const val LOGO_32 = "/org/myteer/novel/image/logo/book_32.png"
        private const val LOGO_64 = "/org/myteer/novel/image/logo/book_64.png"
        private const val LOGO_128 = "/org/myteer/novel/image/logo/book_128.png"
        private const val LOGO_256 = "/org/myteer/novel/image/logo/book_256.png"
        private const val LOGO_512 = "/org/myteer/novel/image/logo/book_512.png"

        private val logger = LoggerFactory.getLogger(BaseWindow::class.java)
    }
}