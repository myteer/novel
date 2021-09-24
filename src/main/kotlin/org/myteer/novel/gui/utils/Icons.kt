package org.myteer.novel.gui.utils

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView
import javafx.scene.text.Text
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("Icons")

private val iconPack = mapOf<String, () -> Text>(
    "info-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.INFORMATION),
    "info-outline-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.INFORMATION_OUTLINE),
    "warning-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.ALERT),
    "warning-circle-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.ALERT_CIRCLE),
    "close-circle-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE),
    "close-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.CLOSE),
    "close-box-multiple-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.CLOSE_BOX),
    "json-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.JSON),
    "file-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.FILE),
    "file-export-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.EXPORT),
    "folder-open-icon" to fun() = FontAwesomeIconView(FontAwesomeIcon.FOLDER_OPEN),
    "database-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.DATABASE),
    "database-plus-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.DATABASE_PLUS),
    "database-minus-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.DATABASE_MINUS),
    "play-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.PLAY),
    "login-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.LOGIN),
    "logout-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.LOGOUT),
    "settings-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.SETTINGS),
    "update-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.UPDATE),
    "puzzle-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.PUZZLE),
    "book-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.BOOK),
    "newspaper-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.NEWSPAPER),
    "book-preview-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.BOOK_OPEN_VARIANT),
    "image-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.IMAGE),
    "copy-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.CONTENT_COPY),
    "paste-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.CONTENT_PASTE),
    "cut-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.CONTENT_CUT),
    "home-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.HOME),
    "arrow-up-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.ARROW_UP),
    "arrow-down-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.ARROW_DOWN),
    "arrow-left-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.ARROW_LEFT),
    "arrows-left-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.STEP_BACKWARD_2),
    "arrow-right-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.ARROW_RIGHT),
    "arrows-right-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.STEP_FORWARD_2),
    "view-list-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.VIEW_LIST),
    "regex-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.REGEX),
    "keyboard-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.KEYBOARD),
    "case-sensitive-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.CASE_SENSITIVE_ALT),
    "google-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.GOOGLE),
    "plus-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.PLUS),
    "plus-box-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.PLUS_BOX),
    "link-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.LINK_VARIANT),
    "link-off-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.LINK_VARIANT_OFF),
    "reload-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.RELOAD),
    "code-braces-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.CODE_BRACES),
    "github-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.GITHUB_BOX),
    "email-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.EMAIL),
    "twitter-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.TWITTER),
    "contact-mail-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.CONTACT_MAIL),
    "search-icon" to fun() = FontAwesomeIconView(FontAwesomeIcon.SEARCH),
    "delete-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.DELETE),
    "delete-forever-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.DELETE_FOREVER),
    "border-top-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.BORDER_TOP),
    "columns-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.VIEW_COLUMN),
    "table-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.TABLE),
    "dock-bottom-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.BORDER_BOTTOM),
    "translate-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.TRANSLATE),
    "database-clock-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.HISTORY),
    "paint-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.FORMAT_PAINT),
    "library-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.LIBRARY),
    "full-screen-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.FULLSCREEN),
    "clipboard-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.CLIPBOARD),
    "save-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.CONTENT_SAVE),
    "pencil-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.PENCIL),
    "modules-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.VIEW_MODULE),
    "star-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.STAR),
    "palette-advanced-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.PALETTE_ADVANCED),
    "details-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.DETAILS),
    "tune-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.TUNE),
    "buffer-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.BUFFER),
    "numeric-2-box-multiple" to fun() = MaterialDesignIconView(MaterialDesignIcon.NUMERIC_2_BOX_MULTIPLE_OUTLINE),
    "rotate-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.ROTATE_3D),
    "duplicate-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.CONTENT_DUPLICATE),
    "excel-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.FILE_EXCEL),
    "bold-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.FORMAT_BOLD),
    "italic-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.FORMAT_ITALIC),
    "strikethrough-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.FORMAT_STRIKETHROUGH),
    "pause-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.PAUSE),
    "stop-icon" to fun() = MaterialDesignIconView(MaterialDesignIcon.STOP)
)

fun icon(identifier: String): Text = (iconPack[identifier]?.invoke() ?: Text().apply {
    logger.error("couldn't find icon for '{}'", identifier)
}).styleClass(identifier)

