package org.myteer.novel.gui.preferences.pane

import javafx.concurrent.Task
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.scene.control.ContentDisplay
import org.myteer.novel.config.Preferences
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.gui.utils.runOutsideUIAsync
import org.myteer.novel.gui.utils.typeEquals
import org.myteer.novel.i18n.i18n
import org.myteer.novel.main.ApplicationRestart
import org.slf4j.LoggerFactory

class AdvancedPane(private val context: Context, preferences: Preferences) : PreferencesPane(preferences) {
    override val title: String = i18n("preferences.tab.advanced")
    override val graphic: Node = icon("palette-advanced-icon")

    override fun buildContent(): Content = object : Content() {
        init {
            items.add(buildGCControl())
            items.add(buildResetControl())
        }

        private fun buildGCControl(): PreferencesControl {
            return PairControl(
                i18n("preferences.advanced.gc"),
                i18n("preferences.advanced.gc.desc"),
                Button("System.gc()").apply {
                    setOnAction {
                        System.gc()
                        logger.debug("Garbage collection request made.")
                    }
                }
            )
        }

        private fun buildResetControl(): PreferencesControl {
            return PairControl(
                i18n("preferences.advanced.reset"),
                i18n("preferences.advanced.reset.desc"),
                Button().apply {
                    contentDisplay = ContentDisplay.GRAPHIC_ONLY
                    graphic = icon("delete-forever-icon")
                    setOnAction {
                        context.showConfirmationDialog(
                            i18n("preferences.advanced.reset.confirm.title"),
                            i18n("preferences.advanced.reset.confirm.message")
                        ) {
                            if (it.typeEquals(ButtonType.YES)) {
                                reset()
                            }
                        }
                    }
                }
            )
        }
    }

    private fun reset() {
        runOutsideUIAsync(object : Task<Unit>() {
            init {
                setOnRunning { context.showIndeterminateProgress() }
                setOnFailed {
                    context.stopProgress()
                    logger.error("Couldn't reset the application", it.source.exception)
                }
                setOnSucceeded {
                    ApplicationRestart.restart()
                }
            }

            override fun call() {
                preferences.editor().reset()
            }
        })
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AdvancedPane::class.java)
    }
}