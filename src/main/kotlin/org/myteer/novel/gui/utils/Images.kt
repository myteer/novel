package org.myteer.novel.gui.utils

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.image.Image
import java.io.BufferedInputStream
import java.util.function.Consumer
import kotlin.reflect.KClass

fun KClass<*>.loadImage(resource: String): Image {
    return Image(this.java.getResourceAsStream(resource)?.let { BufferedInputStream(it) })
}

fun asyncLoadImage(resource: String, onReady: Consumer<Image>) {
    val image = Image(resource, true)
    image.progressProperty().addListener(object : ChangeListener<Number> {
        override fun changed(observable: ObservableValue<out Number>, oldValue: Number, newValue: Number) {
            if (1.0 == newValue && image.isError.not()) {
                onReady.accept(image)
                observable.removeListener(this)
            }
        }
    })
}