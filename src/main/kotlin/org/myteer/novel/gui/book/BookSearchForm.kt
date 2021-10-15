package org.myteer.novel.gui.book

import animatefx.animation.FadeInUp
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.scene.Group
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import org.myteer.novel.crawl.vo.BookSearchRequest
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.control.SearchTextField
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.gui.utils.onScenePresent
import org.myteer.novel.i18n.i18n

class BookSearchForm(
    private val context: Context,
    private val onSearchRequest: (BookSearchRequest) -> Unit
) : StackPane() {
    private val keyword: StringProperty = SimpleStringProperty()

    init {
        styleClass.add("book-search-form")
        buildUI()
        playAnimation()
    }

    private fun buildUI() {
        children.add(buildCenterBox())
    }

    private fun buildCenterBox() = Group(
        VBox(
            10.0,
            buildLogo(),
            buildTitleLabel(),
            buildDescriptionLabel(),
            buildKeywordTextField(),
            buildSearchButton()
        )
    )

    private fun buildLogo() = ImageView().run {
        image = Image("/org/myteer/novel/image/other/biquge_64.png")
        StackPane(this)
    }

    private fun buildTitleLabel() = Label().run {
        text = i18n("book.search.title")
        styleClass.add("title-label")
        StackPane(this)
    }

    private fun buildDescriptionLabel() = Label().run {
        text = i18n("book.search.description")
        StackPane(this)
    }

    private fun buildKeywordTextField() = SearchTextField().apply {
        textProperty().bindBidirectional(keyword)
        promptText = i18n("boot.search.keyword.prompt")
        setOnAction { search() }
    }

    private fun buildSearchButton() = Button().apply {
        text = i18n("book.search.button")
        graphic = icon("search-icon")
        maxWidth = Double.MAX_VALUE
        isDefaultButton = true
        disableProperty().bind(Bindings.createBooleanBinding({ keyword.get()?.isBlank() ?: true }, keyword))
        setOnAction { search() }
    }

    private fun playAnimation() {
        onScenePresent {
            FadeInUp(this).play()
        }
    }

    private fun search() {
        keyword.get().takeIf { it.isNotBlank() }?.let {
            onSearchRequest.invoke(BookSearchRequest(it.trim()))
        }
    }
}