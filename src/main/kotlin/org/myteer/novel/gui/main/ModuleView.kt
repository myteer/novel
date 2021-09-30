package org.myteer.novel.gui.main

import animatefx.animation.FadeInUp
import javafx.css.PseudoClass
import javafx.scene.Group
import javafx.scene.control.*
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import org.myteer.novel.gui.control.tabview.TabItem
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.gui.utils.onScenePresent
import org.myteer.novel.i18n.i18n
import org.myteer.novel.main.PropertiesSetup

class ModuleView(private val mainView: MainView) : StackPane() {
    companion object {
        private const val TILES_PER_ROW = 3
        private const val ROWS_PER_PAGE = 3

        fun getTabItem(mainView: MainView) = TabItem(
            "moduleview",
            i18n("main_view.home"),
            { icon("home-icon") },
            { ModuleView(mainView) }
        )
    }

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

    private fun buildGridPanes(): List<GridPane> = buildTiles()
        .chunked(TILES_PER_ROW)
        .chunked(ROWS_PER_PAGE)
        .map { rowsPerGrid ->
            GridPane().apply {
                styleClass.add("tile-grid")
                rowsPerGrid.forEach {
                    addRow(rowCount, *it.toTypedArray())
                }
            }
        }

    private fun buildTiles(): List<Tile> = mainView.modules.map(::buildTile)

    private fun buildTile(module: Module) = Tile(mainView, module)

    private fun playAnimation() {
        onScenePresent {
            FadeInUp(this).play()
        }
    }

    private class Tile(val mainView: MainView, val module: Module) : VBox() {
        init {
            styleClass.add("tile")
            id = "tile-${module.id}"
            spacing = 10.0
            buildUI()
        }

        private fun buildUI() {
            children.add(StackPane(buildButton()))
            children.add(StackPane(buildLabel()))
        }

        private fun buildButton() = Button().apply {
            contentDisplay = ContentDisplay.GRAPHIC_ONLY
            graphic = module.icon
            tooltip = Tooltip(module.name)
            setOnAction {
                mainView.openTab(module.getTabItem())
            }
        }

        private fun buildLabel() = Label().apply {
            text = module.name
            tooltip = Tooltip(module.name)
        }
    }
}