package org.myteer.novel.gui.crawl.details

import javafx.scene.control.ContentDisplay
import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import org.myteer.novel.crawl.model.Book
import org.myteer.novel.gui.utils.asyncLoadImage
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.gui.utils.styleClass
import org.myteer.novel.i18n.i18n

class CrawlBookDetailsPane(private val book: Book) : VBox() {
    init {
        styleClass.add("crawl-book-details-pane")
        prefWidth = 600.0
        buildUI()
    }

    private fun buildUI() {
        children.add(buildTopPane(book))
        children.add(buildDescriptionPane(book))
        children.add(buildIndexPane(book))
    }

    private fun buildTopPane(book: Book): GridPane = object : GridPane() {
        init {
            styleClass.add("crawl-book-details-group")
            children.addAll(
                buildThumbnail(),
                buildNameLabel(),
                buildAuthorLabel(),
                buildCategoryLabel(),
                buildStatusLabel(),
                buildScoreLabel()
            )
        }

        private fun buildThumbnail() = Label().apply {
            styleClass.add("book-thumbnail-label")
            contentDisplay = ContentDisplay.GRAPHIC_ONLY
            graphic = icon("image-icon").styleClass("thumbnail-place-holder")
            book.thumbnail?.let { url ->
                asyncLoadImage(url) {
                    graphic = ImageView(it).apply {
                        fitWidth = 120.0
                        fitHeight = 150.0
                        isPreserveRatio = true
                    }
                }
            }
            setConstraints(this, 0, 0, 1, 5)
        }

        private fun buildNameLabel() = Label().apply {
            styleClass.add("book-name-label")
            text = book.name ?: ""
            setConstraints(this, 1, 0)
        }

        private fun buildAuthorLabel() = Label().apply {
            text = i18n("crawl.book.details.author", book.author ?: "")
            setConstraints(this, 1, 1)
        }

        private fun buildCategoryLabel() = Label().apply {
            text = i18n("crawl.book.details.category", book.categoryName ?: "")
            setConstraints(this, 1, 2)
        }

        private fun buildStatusLabel() = Label().apply {
            text = i18n("crawl.book.details.status", book.status ?: "")
            setConstraints(this, 1, 3)
        }

        private fun buildScoreLabel() = Label().run {
            text = i18n("crawl.book.details.score", book.score ?: "")
            VBox(this).apply {
                setConstraints(this, 1, 4)
            }
        }
    }

    private fun buildDescriptionPane(book: Book): VBox = object : VBox() {
        init {
            styleClass.add("crawl-book-details-group")
            children.add(buildTitle())
            children.add(buildDescription())
        }

        private fun buildTitle() = Label().apply {
            styleClass.add("title-label")
            text = i18n("crawl.book.details.description")
        }

        private fun buildDescription() = Label(book.description ?: "")
    }

    private fun buildIndexPane(book: Book): GridPane = object : GridPane() {
        init {
            styleClass.add("crawl-book-details-group")
            children.addAll(
                buildTitle(),
                buildIndexIcon(),
                buildLastUpdate(),
                buildLastChapter(),
                buildRightIcon()
            )
        }

        private fun buildTitle() = Label().apply {
            styleClass.add("title-label")
            text = i18n("crawl.book.details.index")
            setConstraints(this, 0, 0, 3, 1)
        }

        private fun buildIndexIcon() = icon("view-list-icon").apply {
            setConstraints(this, 0, 1, 1, 2)
        }

        private fun buildLastUpdate() = Label().apply {
            text = i18n("crawl.book.details.last_update", book.lastUpdateTime ?: "")
            setConstraints(this, 1, 1)
            setHgrow(this, Priority.ALWAYS)
        }

        private fun buildLastChapter() = Label().apply {
            text = book.lastChapterName ?: ""
            setConstraints(this, 1, 2)
            setHgrow(this, Priority.ALWAYS)
        }

        private fun buildRightIcon() = icon("arrow-forward-icon").apply {
            setConstraints(this, 2, 1, 1, 2)
        }
    }
}