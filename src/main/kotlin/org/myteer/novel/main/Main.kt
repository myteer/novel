package org.myteer.novel.main

import com.sun.javafx.application.LauncherImpl
import javafx.application.Application
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.VBox
import javafx.stage.Stage
import javafx.util.Duration
import jfxtras.styles.jmetro.JMetro
import jfxtras.styles.jmetro.JMetroStyleClass
import jfxtras.styles.jmetro.Style
import org.myteer.novel.gui.base.BaseView

class Main : Application() {
    override fun start(stage: Stage) {
        stage.scene = Scene(MainView()).apply {
            stylesheets.add(Main::class.java.getResource("/org/myteer/novel/gui/theme/light.css")?.toExternalForm())
            JMetro(Style.LIGHT).scene = this
        }
        //stage.width = 700.0
        //stage.height = 450.0
        stage.show()
    }

}

class MainView : BaseView() {
    init {
        setContent(buildUI())
        styleClass.add(JMetroStyleClass.BACKGROUND)
    }

    private fun buildUI(): Node {
        val btn1 = Button("Show Information Dialog").apply {
            setOnAction {
                showInformationDialog("标题", "消息") {
                    println("information btn $it")
                }
            }
        }
        val btn2 = Button("Show Confirmation Dialog").apply {
            setOnAction {
                showConfirmationDialog("标题", "消息") {
                    println("confirmation btn $it")
                }
            }
        }
        val btn3 = Button("Show Error Dialog").apply {
            setOnAction {
                showErrorDialog("标题", "消息", RuntimeException("test")) {
                    println("error btn $it")
                }
            }
        }
        val btn4 = Button("Show Information Notification").apply {
            setOnAction {
                showInformationNotification("标题", "消息", Duration.millis(1000.0))
            }
        }
        val btn5 = Button("Show Error Notification").apply {
            setOnAction {
                showErrorNotification("标题", "消息", Duration.millis(5000.0))
            }
        }
        return VBox(10.0).apply {
            children.addAll(btn1, btn2, btn3, btn4, btn5)
            alignment = Pos.CENTER
        }
    }
}


fun main() {
    PropertiesSetup.setupSystemProperties()
    LauncherImpl.launchApplication(Main::class.java, Preloader::class.java, null)
}