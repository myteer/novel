package org.myteer.novel.export.json

import com.google.gson.*
import javafx.scene.Node
import org.myteer.novel.db.data.Book
import org.myteer.novel.db.data.BookProperty
import org.myteer.novel.export.api.BaseExporter
import org.myteer.novel.export.api.ExportProcessObserver
import org.myteer.novel.gui.export.ConfigurationDialog
import org.myteer.novel.gui.export.json.JsonConfigurationDialog
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.i18n.i18n
import java.io.OutputStream
import java.lang.reflect.Type

class JsonExporter : BaseExporter<JsonExportConfiguration>() {
    override val name = "JSON"
    override val icon: Node get() = icon("json-icon")
    override val configurationDialog: ConfigurationDialog<JsonExportConfiguration> get() = JsonConfigurationDialog()
    override val contentType = "json"
    override val contentTypeDescription = i18n("file.content_type.desc.json")

    override fun write(
        items: List<Book>,
        output: OutputStream,
        config: JsonExportConfiguration,
        observer: ExportProcessObserver
    ) {
        output.bufferedWriter().use {
            val gson = buildGson(config)
            gson.toJson(gson.toJsonTree(sortBooks(items, config), List::class.java), it)
        }
    }

    private fun buildGson(config: JsonExportConfiguration): Gson = GsonBuilder().run {
        config.prettyPrinting.takeUnless(Boolean::not)?.let { setPrettyPrinting() }
        config.nonExecutableJson.takeUnless(Boolean::not)?.let { generateNonExecutableJson() }
        config.serializeNulls.takeUnless(Boolean::not)?.let { serializeNulls() }
        registerTypeAdapter(Book::class.java, BookSerializer(config))
        create()
    }

    private class BookSerializer(private val config: JsonExportConfiguration) : JsonSerializer<Book> {
        private val internalGson: Gson = GsonBuilder().run {
            config.serializeNulls.takeUnless(Boolean::not)?.let { serializeNulls() }
            create()
        }

        override fun serialize(src: Book?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
            val serialized = internalGson.toJsonTree(src) as JsonObject

            val propsToRemove = BookProperty.allProperties - config.requiredFields
            propsToRemove.map(BookProperty<*>::id).forEach(serialized::remove)

            return serialized
        }
    }
}