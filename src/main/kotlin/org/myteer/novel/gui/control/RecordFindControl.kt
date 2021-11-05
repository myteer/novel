package org.myteer.novel.gui.control

import javafx.beans.property.*
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.concurrent.Task
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.StackPane
import org.myteer.novel.db.data.Book
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.gui.utils.runOutsideUI
import org.myteer.novel.i18n.i18n
import java.util.regex.PatternSyntaxException

class RecordFindControl(private val baseItems: ObservableList<Book>) : HBox() {
    private val onNewResultsProperty: ObjectProperty<(List<Book>) -> Unit> = SimpleObjectProperty()
    private val onCloseRequestProperty: ObjectProperty<() -> Unit> = SimpleObjectProperty()
    private val resultsCount: IntegerProperty = SimpleIntegerProperty()
    private val caseSensitive: BooleanProperty = SimpleBooleanProperty()
    private val errorMessage: StringProperty = SimpleStringProperty()

    private val baseText: StringProperty = object : SimpleStringProperty() {
        override fun invalidated() {
            search {
                onNewResults?.invoke(it)
                resultsCount.set(it.size)
            }
        }
    }

    var onNewResults: ((List<Book>) -> Unit)?
        get() = onNewResultsProperty.get()
        set(value) {
            onNewResultsProperty.set(value)
        }

    var onCloseRequest: (() -> Unit)?
        get() = onCloseRequestProperty.get()
        set(value) {
            onCloseRequestProperty.set(value)
        }

    private val filter: ObjectProperty<Filter> =
        object : SimpleObjectProperty<Filter>(SimpleFilter(baseText, caseSensitive)) {
            override fun invalidated() {
                search {
                    onNewResults?.invoke(it)
                    resultsCount.set(it.size)
                }
            }
        }

    private val baseItemsChangeListener = ListChangeListener<Book> {
        search {
            onNewResults?.invoke(it)
            resultsCount.set(it.size)
        }
    }

    init {
        styleClass.add("record-find-control")
        padding = Insets(5.0)
        spacing = 5.0
        baseItems.addListener(baseItemsChangeListener)
        buildUI()
    }

    fun releaseListeners() {
        baseItems.removeListener(baseItemsChangeListener)
    }

    override fun requestFocus() {
        children[0].requestFocus()
    }

    private fun buildUI() {
        val group = ToggleGroup()
        children.addAll(
            buildField(),
            buildRegexToggle(group),
            buildExactToggle(group),
            buildCaseToggle(),
            buildSeparator(),
            buildResultLabel(),
            buildSeparator(),
            buildErrorLabel(),
            buildCloseButton()
        )
    }

    private fun buildField(): TextField = SearchTextField().apply {
        baseText.bind(textProperty())
        prefHeight = 32.0
    }

    private fun buildRegexToggle(group: ToggleGroup) = ToggleButton().apply {
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
        graphic = icon("regex-icon")
        tooltip = Tooltip(i18n("record.find.regex"))
        toggleGroup = group
        selectedProperty().addListener { _, _, newValue ->
            filter.set(if (newValue) RegexFilter(baseText, caseSensitive) else SimpleFilter(baseText, caseSensitive))
        }
    }

    private fun buildExactToggle(group: ToggleGroup) = ToggleButton().apply {
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
        graphic = icon("keyboard-icon")
        tooltip = Tooltip(i18n("record.find.exact"))
        toggleGroup = group
        selectedProperty().addListener { _, _, newValue ->
            filter.set(if (newValue) ExactFilter(baseText, caseSensitive) else SimpleFilter(baseText, caseSensitive))
        }
    }

    private fun buildCaseToggle() = ToggleButton().apply {
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
        graphic = icon("case-sensitive-icon")
        tooltip = Tooltip(i18n("record.find.case_sensitive"))
        caseSensitive.bind(selectedProperty())
        selectedProperty().addListener { _, _, _ ->
            filter.get().also {
                filter.set(
                    when (it) {
                        is RegexFilter -> RegexFilter(baseText, caseSensitive)
                        is ExactFilter -> ExactFilter(baseText, caseSensitive)
                        else -> SimpleFilter(baseText, caseSensitive)
                    }
                )
            }
        }
    }

    private fun buildSeparator() = Separator(Orientation.VERTICAL)

    private fun buildResultLabel() = Label().apply {
        textProperty().bind(
            resultsCount.asString().concat(" ").concat(i18n("record.find.results"))
        )
    }.let { StackPane(it) }

    private fun buildErrorLabel() = Label().apply {
        styleClass.add("error-label")
        textProperty().bind(errorMessage)
    }.let { StackPane(it) }

    private fun buildCloseButton() = Button().apply {
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
        graphic = icon("close-icon")
        padding = Insets(0.0)
        setOnAction {
            onCloseRequest?.invoke()
        }
        visibleProperty().bind(onCloseRequestProperty.isNotNull)
        StackPane.setAlignment(this, Pos.CENTER_RIGHT)
    }.let {
        StackPane(it).apply {
            setHgrow(this, Priority.ALWAYS)
        }
    }

    private fun search(onItemsAvailable: (List<Book>) -> Unit) {
        runOutsideUI(object : Task<List<Book>>() {
            init {
                setOnRunning {
                    errorMessage.set(null)
                    showProgress()
                }
                setOnFailed {
                    errorMessage.set(null)
                    stopProgress()
                    onItemsAvailable(emptyList())
                    when (it.source.exception) {
                        is PatternSyntaxException -> errorMessage.set(i18n("record.find.invalid_regex"))
                    }
                }
                setOnSucceeded {
                    errorMessage.set(null)
                    stopProgress()
                    onItemsAvailable(value)
                }
            }

            override fun call(): List<Book> = baseItems.filter { filter.get().filter(it) }
        })
    }

    private fun showProgress() {
        ProgressIndicator(ProgressIndicator.INDETERMINATE_PROGRESS).apply {
            prefWidth = 15.0
            prefHeight = 15.0
        }.let(children::add)
    }

    private fun stopProgress() {
        children.removeIf { it is ProgressIndicator }
    }

    private abstract class Filter(
        private val baseText: StringProperty,
        private val caseSensitive: BooleanProperty
    ) {
        abstract fun checkMatch(userInput: String, bookValue: String, ignoreCase: Boolean): Boolean

        fun filter(book: Book): Boolean =
            book.values().find { checkMatch(baseText.get()?.trim() ?: "", it, !caseSensitive.get()) } != null
    }

    private class SimpleFilter(
        baseText: StringProperty,
        caseSensitive: BooleanProperty
    ) : Filter(baseText, caseSensitive) {
        override fun checkMatch(userInput: String, bookValue: String, ignoreCase: Boolean): Boolean =
            bookValue.contains(userInput, ignoreCase)
    }

    private class ExactFilter(
        baseText: StringProperty,
        caseSensitive: BooleanProperty
    ) : Filter(baseText, caseSensitive) {
        override fun checkMatch(userInput: String, bookValue: String, ignoreCase: Boolean): Boolean =
            bookValue.equals(userInput, ignoreCase)
    }

    private class RegexFilter(
        baseText: StringProperty,
        caseSensitive: BooleanProperty
    ) : Filter(baseText, caseSensitive) {
        override fun checkMatch(userInput: String, bookValue: String, ignoreCase: Boolean): Boolean =
            compileRegex(userInput, ignoreCase).matches(bookValue)

        private fun compileRegex(userInput: String, ignoreCase: Boolean) =
            Regex(userInput, if (ignoreCase) setOf(RegexOption.IGNORE_CASE) else emptySet())
    }
}