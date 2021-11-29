package org.myteer.novel.gui.base

import com.dlsc.workbenchfx.Workbench
import com.dlsc.workbenchfx.WorkbenchSkin
import com.dlsc.workbenchfx.model.WorkbenchDialog
import com.dlsc.workbenchfx.model.WorkbenchModule
import com.dlsc.workbenchfx.view.WorkbenchView
import com.dlsc.workbenchfx.view.controls.module.Tile
import com.nativejavafx.taskbar.TaskbarProgressbar
import com.nativejavafx.taskbar.TaskbarProgressbarFactory
import com.nativejavafx.taskbar.exception.StageNotShownException
import javafx.application.Platform
import javafx.beans.property.*
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import javafx.event.Event
import javafx.event.EventHandler
import javafx.scene.Cursor
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.ButtonType
import javafx.scene.control.Hyperlink
import javafx.scene.control.Label
import javafx.scene.control.Skin
import javafx.scene.image.Image
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import javafx.stage.Window
import javafx.util.Duration
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.api.ContextDialog
import org.myteer.novel.gui.control.ExceptionDisplayPane
import org.myteer.novel.gui.utils.I18NButtonType
import org.myteer.novel.gui.utils.onWindowPresent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.awt.Toolkit
import java.util.function.Consumer

open class BaseView(content: Node? = null) : StackPane(), Context {
    private val content = SimpleObjectProperty<Node>()
    private val notificationsBox = NotificationsBox()
    private val workbench: Workbench
    private var taskbarProgressbarCache: TaskbarProgressbar? = null

    init {
        workbench = buildWorkbench()
        children.add(workbench)
        setContent(content)
    }

    private fun buildWorkbench(): Workbench {
        return initWorkbench(Workbench.builder(object : WorkbenchModule(null, null as Image?) {
            override fun activate(): Node {
                return content.get()
            }
        }).tileFactory { Tile(it).apply { isVisible = false } }.build())
    }

    @Suppress("UNCHECKED_CAST")
    private fun initWorkbench(workbench: Workbench): Workbench {
        workbench.skinProperty().addListener(object : ChangeListener<WorkbenchSkin> {
            override fun changed(observable: ObservableValue<out WorkbenchSkin>?, oldValue: WorkbenchSkin?, skin: WorkbenchSkin?) {
                skin?.let {
                    try {
                        val field = WorkbenchSkin::class.java.getDeclaredField("workbenchView").apply {
                            isAccessible = true
                        }
                        val workbenchView = field.get(it) as WorkbenchView
                        workbenchView.children.add(notificationsBox)
                        it.node.lookup("#toolbar")?.apply {
                            isVisible = false
                            isManaged = false
                        }
                    } catch (e: Exception) {
                        logger.error("couldn't retrieve WorkbenchView", e)
                    }
                    observable?.removeListener(this)
                }
            }
        } as ChangeListener<in Skin<*>>)
        return workbench
    }

    fun contentProperty(): ObjectProperty<Node> {
        return content
    }

    fun setContent(node: Node?) {
        content.set(node)
    }

    fun getContent(): Node? {
        return content.get()
    }

    override fun showOverlay(region: Region, blocking: Boolean) {
        workbench.showOverlay(region, blocking)
    }

    override fun hideOverlay(region: Region) {
        workbench.hideOverlay(region)
    }

    override fun showInformationDialog(title: String, message: String, onResult: Consumer<ButtonType>): ContextDialog {
        val workbenchDialog = WorkbenchDialog.builder(title, message, I18NButtonType.OK)
            .onResult(onResult)
            .build()
        return WorkbenchDialogContextDialog(workbench.showDialog(workbenchDialog), ContextDialog.Type.INFORMATION)
    }

    override fun showConfirmationDialog(title: String, message: String, onResult: Consumer<ButtonType>): ContextDialog {
        val workbenchDialog = WorkbenchDialog.builder(title, message, I18NButtonType.NO, I18NButtonType.YES)
            .onResult(onResult)
            .build()
        return WorkbenchDialogContextDialog(workbench.showDialog(workbenchDialog), ContextDialog.Type.CONFIRMATION)
    }

    override fun showErrorDialog(title: String, message: String, exception: Exception?, onResult: Consumer<ButtonType>?): ContextDialog {
        val workbenchDialog = exception?.let {
            WorkbenchDialog.builder(title, VBox(Label(message), ExceptionDisplayPane(it)).apply { spacing = 10.0 }, I18NButtonType.OK).onResult(onResult).build()
        } ?: WorkbenchDialog.builder(title, message, I18NButtonType.OK).onResult(onResult).build()
        return WorkbenchDialogContextDialog(workbench.showDialog(workbenchDialog), ContextDialog.Type.ERROR)
    }

    override fun showDialog(title: String, content: Node, onResult: Consumer<ButtonType>, vararg buttonTypes: ButtonType): ContextDialog {
        val workbenchDialog = WorkbenchDialog.builder(title, content, *buttonTypes)
            .onResult(onResult)
            .build()
        return WorkbenchDialogContextDialog(workbench.showDialog(workbenchDialog))
    }

    override fun showInformationDialogAndWait(title: String, message: String): ButtonType {
        val key = Object()
        showInformationDialog(title, message) {
            Platform.exitNestedEventLoop(key, it)
        }
        return Platform.enterNestedEventLoop(key) as ButtonType
    }

    override fun showConfirmationDialogAndWait(title: String, message: String): ButtonType {
        val key = Object()
        showConfirmationDialog(title, message) {
            Platform.exitNestedEventLoop(key, it)
        }
        return Platform.enterNestedEventLoop(key) as ButtonType
    }

    override fun showErrorDialogAndWait(title: String, message: String, exception: Exception?): ButtonType {
        val key = Object()
        showErrorDialog(title, message, exception) {
            Platform.exitNestedEventLoop(key, it)
        }
        return Platform.enterNestedEventLoop(key) as ButtonType
    }

    override fun showDialogAndWait(title: String, content: Node, vararg buttonTypes: ButtonType): ButtonType {
        val key = Object()
        showDialog(title, content, {
            Platform.exitNestedEventLoop(key, it)
        }, *buttonTypes)
        return Platform.enterNestedEventLoop(key) as ButtonType
    }

    override fun showInformationNotification(title: String, message: String, duration: Duration?, onClicked: EventHandler<MouseEvent>?, vararg hyperlinks: Hyperlink) {
        showNotification(NotificationNode.NotificationType.INFO, title, message, duration, hyperlinks, onClicked)
    }

    override fun showWarningNotification(title: String, message: String, duration: Duration?, onClicked: EventHandler<MouseEvent>?) {
        showNotification(NotificationNode.NotificationType.WARNING, title, message, duration, onClicked = onClicked)
    }

    override fun showErrorNotification(title: String, message: String, duration: Duration?, onClicked: EventHandler<MouseEvent>?) {
        showNotification(NotificationNode.NotificationType.ERROR, title, message, duration, onClicked = onClicked)
    }

    override fun getContextScene(): Scene? {
        return workbench.scene
    }

    override fun getContextWindow(): Window? {
        return getContextScene()?.window
    }

    override fun toFrontRequest() {
        val contextWindow = getContextWindow()
        if (contextWindow is Stage) {
            contextWindow.isIconified = false
            contextWindow.toFront()
        }
    }

    override fun isShowing(): Boolean {
        return getContextWindow()?.isShowing ?: false
    }

    override fun showProgress(done: Long, max: Long, type: Context.ProgressType) {
        if (isShowing()) {
            workbench.cursor = Cursor.DEFAULT
            getTaskbarProgressbar().showCustomProgress(done, max, TaskbarProgressbar.Type.valueOf(type.name))
        } else {
            logger.error("progress request on closed BaseView")
        }
    }

    override fun showIndeterminateProgress() {
        if (isShowing()) {
            workbench.cursor = Cursor.WAIT
            getTaskbarProgressbar().showIndeterminateProgress()
        } else {
            logger.error("indeterminate progress request on closed BaseView")
        }
    }

    override fun stopProgress() {
        workbench.cursor = Cursor.DEFAULT
        try {
            getTaskbarProgressbar().stopProgress()
        } catch (e: StageNotShownException) {
            logger.error("used taskbar progress-bar on a closed stage", e)
        }
    }

    override fun onWindowPresent(action: Consumer<Window>) {
        workbench.onWindowPresent(action)
    }

    private fun showNotification(
        type: NotificationNode.NotificationType,
        title: String,
        message: String?,
        duration: Duration? = null,
        hyperlinks: Array<out Hyperlink>? = null,
        onClicked: EventHandler<MouseEvent>? = null
    ) {
        val notificationNode = buildNotificationNode(type, title, message, {
            notificationsBox.removeItem(it)
        }, hyperlinks, onClicked)
        notificationsBox.pushItem(notificationNode, duration)
        playNotificationSound()
    }

    private fun buildNotificationNode(
        type: NotificationNode.NotificationType,
        title: String,
        message: String?,
        closeAction: Consumer<NotificationNode>,
        hyperlinks: Array<out Hyperlink>? = null,
        onClicked: EventHandler<MouseEvent>? = null
    ): NotificationNode {
        return NotificationNode(type, title, message, hyperlinks, closeAction).apply {
            setOnMouseClicked {
                onClicked?.handle(it)
                if (!it.isConsumed) {
                    closeAction.accept(this)
                }
            }
        }
    }

    private fun playNotificationSound() {
        Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.hand")?.let {
            (it as Runnable).run()
        }
    }

    private fun getTaskbarProgressbar(): TaskbarProgressbar {
        if (null == taskbarProgressbarCache) {
            taskbarProgressbarCache = TaskbarProgressbarFactory.getTaskbarProgressbar(getContextWindow() as Stage)
        }
        return taskbarProgressbarCache!!
    }

    fun getBlockingOverlaysShown(): ObservableList<Region> = workbench.blockingOverlaysShown

    fun getNonBlockingOverlaysShown(): ObservableList<Region> = workbench.nonBlockingOverlaysShown

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(BaseView::class.java)
    }

    private class WorkbenchDialogContextDialog(val workbenchDialog: WorkbenchDialog, contextDialogType: ContextDialog.Type? = null) : ContextDialog {
        companion object {
            private const val DIALOG_STYLE_CLASS = "alertDialog"
        }

        private val type: ContextDialog.Type?

        init {
            workbenchDialog.styleClass.add(DIALOG_STYLE_CLASS)
            type = if (null != workbenchDialog.type) ContextDialog.Type.valueOf(workbenchDialog.type.toString()) else contextDialogType
            workbenchDialog.showingProperty().addListener { _, ov, nv ->
                if (ov && !nv) {
                    workbenchDialog.onResult = null
                }
            }
        }

        override fun getType(): ContextDialog.Type? {
            return type
        }

        override fun getButtonTypes(): ObservableList<ButtonType> {
            return workbenchDialog.buttonTypes
        }

        override fun getStyleClass(): ObservableList<String> {
            return workbenchDialog.styleClass
        }

        override fun onShownProperty(): ObjectProperty<EventHandler<Event>> {
            return workbenchDialog.onShownProperty()
        }

        override fun setOnShown(handler: EventHandler<Event>) {
            workbenchDialog.onShown = handler
        }

        override fun getOnShown(): EventHandler<Event> {
            return workbenchDialog.onShown
        }

        override fun onHiddenProperty(): ObjectProperty<EventHandler<Event>> {
            return workbenchDialog.onHiddenProperty()
        }

        override fun setOnHidden(handler: EventHandler<Event>) {
            workbenchDialog.onHidden = handler
        }

        override fun getOnHidden(): EventHandler<Event> {
            return workbenchDialog.onHidden
        }

        override fun maximizedProperty(): BooleanProperty {
            return workbenchDialog.maximizedProperty()
        }

        override fun setMaximized(max: Boolean) {
            workbenchDialog.isMaximized = max
        }

        override fun isMaximized(): Boolean {
            return workbenchDialog.isMaximized
        }

        override fun contentProperty(): ObjectProperty<Node> {
            return workbenchDialog.contentProperty()
        }

        override fun setContent(content: Node) {
            workbenchDialog.content = content
        }

        override fun getContent(): Node {
            return workbenchDialog.content
        }

        override fun titleProperty(): StringProperty {
            return workbenchDialog.titleProperty()
        }

        override fun setTitle(title: String) {
            workbenchDialog.title = title
        }

        override fun getTitle(): String {
            return workbenchDialog.title
        }

        override fun exceptionProperty(): ObjectProperty<Exception> {
            return workbenchDialog.exceptionProperty()
        }

        override fun setException(exception: Exception) {
            workbenchDialog.exception = exception
        }

        override fun getException(): Exception {
            return workbenchDialog.exception
        }

        override fun detailsProperty(): StringProperty {
            return workbenchDialog.detailsProperty()
        }

        override fun setDetails(details: String) {
            workbenchDialog.details = details
        }

        override fun getDetails(): String {
            return workbenchDialog.details
        }

        override fun blockingProperty(): BooleanProperty {
            return workbenchDialog.blockingProperty()
        }

        override fun setBlocking(blocking: Boolean) {
            workbenchDialog.isBlocking = blocking
        }

        override fun isBlocking(): Boolean {
            return workbenchDialog.isBlocking
        }

        override fun onResultProperty(): ObjectProperty<Consumer<ButtonType>> {
            return workbenchDialog.onResultProperty()
        }

        override fun setOnResult(onResult: Consumer<ButtonType>) {
            workbenchDialog.onResult = onResult
        }

        override fun getOnResult(): Consumer<ButtonType> {
            return workbenchDialog.onResult
        }

        override fun showingProperty(): ReadOnlyBooleanProperty {
            return workbenchDialog.showingProperty()
        }

        override fun isShowing(): Boolean {
            return workbenchDialog.isShowing
        }
    }
}