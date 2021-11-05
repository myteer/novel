package org.myteer.novel.gui.main

import javafx.stage.WindowEvent
import org.myteer.novel.config.Preferences
import org.myteer.novel.db.DatabaseMeta
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.base.BaseView
import org.myteer.novel.gui.bookmanager.BookManagerModule
import org.myteer.novel.gui.control.tabview.TabItem
import org.myteer.novel.gui.crawl.CrawlBookImportModule
import org.myteer.novel.gui.entry.DatabaseTracker
import org.slf4j.LoggerFactory

class MainView(
    private val preferences: Preferences,
    private val database: NitriteDatabase,
    databaseTracker: DatabaseTracker
) : BaseView() {
    val modules: List<Module> = listOf(
        BookManagerModule(this, preferences, database),
        CrawlBookImportModule(this)
    )

    val databaseMeta: DatabaseMeta
        get() = database.meta

    init {
        styleClass.add("main-view")
        setContent(MainViewBase(this, preferences, databaseTracker))
        initSafetyModuleClosePolicy()
    }

    fun openModuleTab() {
        openTab(ModuleView.getTabItem(this))
    }

    fun openModule(module: Module) {
        openTab(module.getTabItem())
    }

    fun openTab(tabItem: TabItem) {
        (getContent() as MainViewBase).openTab(tabItem)
    }

    fun closeTab(tabItem: TabItem) {
        (getContent() as MainViewBase).closeTab(tabItem)
    }

    override fun sendRequest(request: Context.Request) {
        when (request) {
            is ModuleOpenRequest<*> -> {
                modules.find { it.javaClass == request.clazz }?.let {
                    openModule(it)
                    request.moduleMessage?.let { message ->
                        it.sendMessage(message)
                    }
                }
            }
            is TabItemOpenRequest -> openTab(request.tabItem)
            is TabItemCloseRequest -> closeTab(request.tabItem)
        }
    }

    private fun initSafetyModuleClosePolicy() {
        val shutdownHook = ShutdownHook()
        Runtime.getRuntime().addShutdownHook(shutdownHook)
        super.onWindowPresent {
            logger.debug("Window found! Adding event handler for WINDOW_HIDDEN event.")
            it.addEventHandler(WindowEvent.WINDOW_HIDDEN) {
                logger.debug("Closing modules forcefully...")
                closeModulesForcefully()
                Runtime.getRuntime().removeShutdownHook(shutdownHook)
            }
        }
    }

    private fun closeModulesForcefully() {
        modules.filter(Module::isOpened).forEach {
            try {
                it.close()
            } catch (e: Exception) {
                logger.error("Received exception when trying to close the module with id: '{}'", it.id, e)
            }
        }
    }

    private inner class ShutdownHook : Thread() {
        override fun run() {
            logger.debug("Shutdown hook: closing modules forcefully...")
            closeModulesForcefully()

            preferences.editor().commit()
        }
    }

    class TabItemOpenRequest(val tabItem: TabItem) : Context.Request

    class TabItemCloseRequest(val tabItem: TabItem) : Context.Request

    class ModuleOpenRequest<M : Module>(val clazz: Class<M>, val moduleMessage: Module.Message? = null) :
        Context.Request

    companion object {
        private val logger = LoggerFactory.getLogger(MainView::class.java)
    }
}