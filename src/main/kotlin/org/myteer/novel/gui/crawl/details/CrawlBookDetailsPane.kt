package org.myteer.novel.gui.crawl.details

import animatefx.animation.RotateIn
import javafx.geometry.HPos
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.control.*
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import org.myteer.novel.crawl.model.Book
import org.myteer.novel.gui.control.WrapLabel
import org.myteer.novel.gui.utils.asyncLoadImage
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.gui.utils.styleClass
import org.myteer.novel.i18n.i18n

class CrawlBookDetailsPane(private val view: CrawlBookQueryPane, private val book: Book) : VBox() {
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
        book.sameCategoryBooks.takeIf { it?.isNotEmpty() ?: false }?.let {
            children.add(buildSameCategoryBooksPane(it))
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
                buildBookImportButton(),
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
            setConstraints(this, 0, 0, 1, 4)
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
            setVgrow(this, Priority.ALWAYS)
            setValignment(this, VPos.TOP)
        }

        private fun buildBookImportButton() = Button().apply {
            text = i18n("crawl.book.import.title")
            isDefaultButton = true
            setOnAction {
                TODO()
            }
            setConstraints(this, 2, 0)
            setHgrow(this, Priority.ALWAYS)
            setHalignment(this, HPos.RIGHT)
        }

        private fun buildScoreLabel() = Label().apply {
            styleClass.add("score-label")
            text = book.score?.toString().orEmpty()
            graphic = icon("star-face-icon")
            contentDisplay = ContentDisplay.RIGHT
            setConstraints(this, 2, 1, 1, 2)
            setHgrow(this, Priority.ALWAYS)
            setHalignment(this, HPos.RIGHT)
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
            children.add(buildTitle())
            books.forEachIndexed { index, book ->
                children.add(SameAuthorBook(book))
                if (index != books.size - 1) {
                    children.add(Separator())
                }
            }
        }

        private fun buildTitle() = Label().apply {
            styleClass.add("title-label")
            text = i18n("crawl.book.details.same_author_books", book.author.orEmpty())
        }
    }

    private fun buildSameCategoryBooksPane(books: List<Book>): VBox = object : VBox() {
        private val items: MutableList<SameCategoryBook> = mutableListOf()
        private val titleLabel = buildTitle()
        private val refreshButton = buildRefreshButton()
        private val contentPane = HBox(10.0)

        init {
            styleClass.add("same-category-books-group")
            initItems()
            buildUI()
        }

        private fun initItems() {
            books.forEach { items.add(SameCategoryBook(it)) }
        }

        private fun buildUI() {
            children.add(BorderPane().apply {
                left = titleLabel
                right = refreshButton
            })
            children.add(contentPane)
            refresh()
        }

        private fun buildTitle() = Label().apply {
            styleClass.add("title-label")
            text = i18n("crawl.book.details.same_category_books")
        }

        private fun buildRefreshButton() = Button().apply {
            styleClass.add("refresh-button")
            contentDisplay = ContentDisplay.GRAPHIC_ONLY
            graphic = icon("reload-icon")
            tooltip = Tooltip(i18n("crawl.book.details.same_category_books.refresh"))
            setOnAction {
                refresh()
                RotateIn(graphic).play()
            }
        }

        private fun refresh() {
            contentPane.children.clear()
            items.shuffled().take(5).forEach {
                contentPane.children.add(it)
            }
        }
    }

    private inner class SameAuthorBook(private val book: Book) : HBox() {
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
            setOnMouseClicked { view.loadData(book.id!!) }
            setMinSize(96.0, 120.0)
            setMaxSize(96.0, 120.0)
        }

        private fun buildInfoPane() = VBox(10.0).apply {
            children.addAll(
                buildNameLabel(),
                buildAuthorLabel(),
                buildCategoryLabel()
            )
            setHgrow(this, Priority.ALWAYS)
            alignment = Pos.CENTER_LEFT
        }

        private fun buildNameLabel() = Label(book.name.orEmpty()).styleClass("name-label")

        private fun buildAuthorLabel() = Label(book.author.orEmpty())

        private fun buildCategoryLabel() = Label(book.lastChapterName.orEmpty())
    }

    private inner class SameCategoryBook(private val book: Book) : VBox() {
        private val thumbnail = buildThumbnail()
        private val nameLabel = buildNameLabel()

        init {
            styleClass.add("same-category-book")
            alignment = Pos.BOTTOM_CENTER
            children.add(thumbnail)
            children.add(nameLabel)
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
            setOnMouseClicked { view.loadData(book.id!!) }
            setMinSize(96.0, 120.0)
            setMaxSize(96.0, 120.0)
        }

        private fun buildNameLabel() = Label(book.name.orEmpty()).styleClass("name-label")
    }
}