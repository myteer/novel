package org.myteer.novel.gui.crawl.details

import javafx.scene.control.ContentDisplay
import javafx.scene.control.Label
import javafx.scene.control.Separator
import javafx.scene.image.ImageView
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import org.myteer.novel.crawl.model.Book
import org.myteer.novel.gui.control.WrapLabel
import org.myteer.novel.gui.utils.asyncLoadImage
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.gui.utils.styleClass
import org.myteer.novel.i18n.i18n

class CrawlBookDetailsPane(private val book: Book) : VBox() {
    init {
        styleClass.add("crawl-book-details-pane")
        buildUI()
    }

    private fun buildUI() {
        children.addAll(
            buildTopPane(book),
            buildDescriptionPane(book),
            buildIndexPane(book)
        )
        book.sameAuthorBooks.takeIf { it?.isNotEmpty() ?: false }?.let {
            children.add(buildSameAuthorBooksPane(it))
        }
    }

    private fun buildTopPane(book: Book): GridPane = object : GridPane() {
        init {
            styleClass.add("top-group")
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
            styleClass.add("thumbnail-label")
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
            setMinSize(120.0, 150.0)
            setMaxSize(120.0, 150.0)
            setConstraints(this, 0, 0, 1, 5)
        }

        private fun buildNameLabel() = Label().apply {
            styleClass.add("name-label")
            text = book.name.orEmpty()
            setConstraints(this, 1, 0)
        }

        private fun buildAuthorLabel() = Label().apply {
            text = i18n("crawl.book.details.author", book.author.orEmpty())
            setConstraints(this, 1, 1)
        }

        private fun buildCategoryLabel() = Label().apply {
            text = i18n("crawl.book.details.category", book.categoryName.orEmpty())
            setConstraints(this, 1, 2)
        }

        private fun buildStatusLabel() = Label().apply {
            text = i18n("crawl.book.details.status", book.status.orEmpty())
            setConstraints(this, 1, 3)
        }

        private fun buildScoreLabel() = Label().run {
            text = i18n("crawl.book.details.score", book.score?.toString().orEmpty())
            VBox(this).apply {
                setConstraints(this, 1, 4)
            }
        }
    }

    private fun buildDescriptionPane(book: Book): VBox = object : VBox() {
        init {
            styleClass.add("description-group")
            children.add(buildTitle())
            children.add(buildDescription())
        }

        private fun buildTitle() = Label().apply {
            styleClass.add("title-label")
            text = i18n("crawl.book.details.description")
        }

        private fun buildDescription() = WrapLabel(book.description.orEmpty(), 55.0)
    }

    private fun buildIndexPane(book: Book): GridPane = object : GridPane() {
        init {
            styleClass.add("index-group")
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
            styleClass.add("last-update-label")
            text = i18n("crawl.book.details.last_update", book.lastUpdateTime.orEmpty())
            setConstraints(this, 1, 1)
            setHgrow(this, Priority.ALWAYS)
        }

        private fun buildLastChapter() = Label().apply {
            text = book.lastChapterName.orEmpty()
            setConstraints(this, 1, 2)
            setHgrow(this, Priority.ALWAYS)
        }

        private fun buildRightIcon() = icon("direction-right-icon").apply {
            setConstraints(this, 2, 1, 1, 2)
        }
    }

    private fun buildSameAuthorBooksPane(books: List<Book>): VBox = object : VBox() {
        init {
            styleClass.add("same-author-books-group")
            buildUI()
        }

        private fun buildUI() {
            books.forEachIndexed { index, book ->
                children.add(SameAuthorBook(book))
                if (index != books.size - 1) {
                    children.add(Separator())
                }
            }
        }
    }

    private class SameAuthorBook(private val book: Book) : HBox() {
        private val thumbnail = buildThumbnail()
        private val infoPane = buildInfoPane()

        init {
            styleClass.add("same-author-book")
            children.add(thumbnail)
            children.add(infoPane)
        }

        private fun buildThumbnail() = Label().apply {
            styleClass.add("thumbnail-label")
            contentDisplay = ContentDisplay.GRAPHIC_ONLY
            graphic = icon("image-icon").styleClass("thumbnail-place-holder")
            book.thumbnail?.let { url ->
                asyncLoadImage(url) {
                    graphic = ImageView(it).apply {
                        fitWidth = 96.0
                        fitHeight = 120.0
                        isPreserveRatio = true
                    }
                }
            }
            setMinSize(96.0, 120.0)
            setMaxSize(96.0, 120.0)
        }

        private fun buildInfoPane() = VBox().apply {
            children.addAll(
                buildNameLabel(),
                buildAuthorLabel(),
                buildCategoryLabel()
            )
            setHgrow(this, Priority.ALWAYS)
        }

        private fun buildNameLabel() = Label(book.name.orEmpty()).styleClass("name-label")

        private fun buildAuthorLabel() = Label(book.author.orEmpty())

        private fun buildCategoryLabel() = Label(book.lastChapterName.orEmpty())
    }
}