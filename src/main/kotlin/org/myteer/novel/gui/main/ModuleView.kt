package org.myteer.novel.gui.main

import animatefx.animation.FadeInUp
import javafx.css.PseudoClass
import javafx.scene.Group
import javafx.scene.control.Label
import javafx.scene.control.Pagination
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import org.myteer.novel.gui.utils.onScenePresent
import org.myteer.novel.main.PropertiesSetup

class ModuleView(private val mainView: MainView) : StackPane() {
    init {
        styleClass.add("module-view")
        buildUI()
        playAnimation()
    }

    private fun buildUI() {
        children.add(buildCenterBox())
    }

    private fun buildCenterBox() = Group(
        VBox(20.0).apply {
            children.add(buildLabelArea())
            children.add(buildPagination())
        }
    )

    private fun buildLabelArea() = StackPane(
        Group(
            HBox(10.0).apply {
                styleClass.add("label-area")
                children.add(StackPane(ImageView()))
                children.add(StackPane(buildAppLabel()))
            }
        )
    )

    private fun buildAppLabel() = Label(System.getProperty(PropertiesSetup.APP_NAME))

    private fun buildPagination() = Pagination().apply {
        VBox.setVgrow(this, Priority.ALWAYS)
        styleClass.addAll(Pagination.STYLE_CLASS_BULLET, "tile-pagination")
        pageCountProperty().addListener { _, _, count ->
            pseudoClassStateChanged(PseudoClass.getPseudoClass("one-page"), count == 1)
        }
        val gridPanes = buildGridPanes()
        pageCount = gridPanes.size
        setPageFactory { gridPanes[it] }
    }

    private fun buildGridPanes(): List<GridPane> {
        TODO()
    }

    private fun playAnimation() {
        onScenePresent {
            FadeInUp(this).play()
        }
    }
}