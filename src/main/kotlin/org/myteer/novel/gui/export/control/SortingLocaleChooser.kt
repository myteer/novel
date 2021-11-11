package org.myteer.novel.gui.export.control

import javafx.scene.control.ChoiceBox
import org.myteer.novel.export.api.BookExportConfiguration
import org.myteer.novel.gui.utils.onValuePresent
import org.myteer.novel.gui.utils.selectedItem
import org.myteer.novel.gui.utils.selectedItemProperty
import org.myteer.novel.gui.utils.valueConvertingPolicy
import org.myteer.novel.i18n.I18N
import java.util.*

class SortingLocaleChooser(exportConfiguration: BookExportConfiguration) : ChoiceBox<Locale>() {
    init {
        maxWidth = Double.MAX_VALUE
        items.addAll(listOf(Locale.forLanguageTag("")) + I18N.getAvailableCollators().keys)
        selectedItem = exportConfiguration.sortLocale
        valueConvertingPolicy(Locale::getDisplayLanguage, Locale::forLanguageTag)
        selectedItemProperty().onValuePresent {
            exportConfiguration.sortLocale = it
        }
    }
}