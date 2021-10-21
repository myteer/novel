package org.myteer.novel.gui.utils

import javafx.scene.text.Text
import org.kordamp.ikonli.javafx.FontIcon
import org.kordamp.ikonli.material2.Material2AL
import org.kordamp.ikonli.material2.Material2MZ
import org.kordamp.ikonli.material2.Material2SharpAL
import org.kordamp.ikonli.materialdesign2.*
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("Icons")

private val iconPack: Map<String, () -> Text> = mapOf(
    "info-icon" to fun() = FontIcon(MaterialDesignI.INFORMATION),
    "info-outline-icon" to fun() = FontIcon(MaterialDesignI.INFORMATION_OUTLINE),
    "warning-icon" to fun() = FontIcon(MaterialDesignA.ALERT),
    "warning-circle-icon" to fun() = FontIcon(MaterialDesignA.ALERT_CIRCLE),
    "close-circle-icon" to fun() = FontIcon(MaterialDesignC.CLOSE_CIRCLE),
    "close-icon" to fun() = FontIcon(MaterialDesignC.CLOSE),
    "close-box-multiple-icon" to fun() = FontIcon(MaterialDesignC.CLOSE_BOX),
    "json-icon" to fun() = FontIcon(MaterialDesignC.CODE_JSON),
    "file-icon" to fun() = FontIcon(MaterialDesignF.FILE),
    "file-export-icon" to fun() = FontIcon(MaterialDesignE.EXPORT),
    "folder-open-icon" to fun() = FontIcon(MaterialDesignF.FOLDER_OPEN),
    "database-icon" to fun() = FontIcon(MaterialDesignD.DATABASE),
    "database-plus-icon" to fun() = FontIcon(MaterialDesignD.DATABASE_PLUS),
    "database-minus-icon" to fun() = FontIcon(MaterialDesignD.DATABASE_MINUS),
    "database-manager-icon" to fun() = FontIcon(MaterialDesignD.DATABASE_COG),
    "play-icon" to fun() = FontIcon(MaterialDesignP.PLAY),
    "login-icon" to fun() = FontIcon(MaterialDesignL.LOGIN),
    "logout-icon" to fun() = FontIcon(MaterialDesignL.LOGOUT),
    "settings-icon" to fun() = FontIcon(Material2MZ.SETTINGS),
    "window-icon" to fun() = FontIcon(Material2AL.LAPTOP_WINDOWS),
    "update-icon" to fun() = FontIcon(MaterialDesignU.UPDATE),
    "puzzle-icon" to fun() = FontIcon(MaterialDesignP.PUZZLE),
    "book-icon" to fun() = FontIcon(MaterialDesignB.BOOK),
    "newspaper-icon" to fun() = FontIcon(MaterialDesignN.NEWSPAPER),
    "book-preview-icon" to fun() = FontIcon(MaterialDesignB.BOOK_OPEN_VARIANT),
    "image-icon" to fun() = FontIcon(MaterialDesignI.IMAGE),
    "copy-icon" to fun() = FontIcon(MaterialDesignC.CONTENT_COPY),
    "paste-icon" to fun() = FontIcon(MaterialDesignC.CONTENT_PASTE),
    "cut-icon" to fun() = FontIcon(MaterialDesignC.CONTENT_CUT),
    "home-icon" to fun() = FontIcon(MaterialDesignH.HOME),
    "arrow-up-icon" to fun() = FontIcon(MaterialDesignA.ARROW_UP),
    "arrow-down-icon" to fun() = FontIcon(MaterialDesignA.ARROW_DOWN),
    "arrow-left-icon" to fun() = FontIcon(MaterialDesignA.ARROW_LEFT),
    "arrow-right-icon" to fun() = FontIcon(MaterialDesignA.ARROW_RIGHT),
    "direction-left-icon" to fun() = FontIcon(Material2AL.KEYBOARD_ARROW_LEFT),
    "direction-right-icon" to fun() = FontIcon(Material2AL.KEYBOARD_ARROW_RIGHT),
    "direction-up-icon" to fun() = FontIcon(Material2AL.KEYBOARD_ARROW_UP),
    "direction-down-icon" to fun() = FontIcon(Material2AL.KEYBOARD_ARROW_DOWN),
    "view-list-icon" to fun() = FontIcon(MaterialDesignV.VIEW_LIST),
    "regex-icon" to fun() = FontIcon(MaterialDesignR.REGEX),
    "keyboard-icon" to fun() = FontIcon(MaterialDesignK.KEYBOARD),
    "case-sensitive-icon" to fun() = FontIcon(MaterialDesignC.CASE_SENSITIVE_ALT),
    "google-icon" to fun() = FontIcon(MaterialDesignG.GOOGLE),
    "plus-icon" to fun() = FontIcon(MaterialDesignP.PLUS),
    "plus-box-icon" to fun() = FontIcon(MaterialDesignP.PLUS_BOX),
    "link-icon" to fun() = FontIcon(MaterialDesignL.LINK_VARIANT),
    "link-off-icon" to fun() = FontIcon(MaterialDesignL.LINK_VARIANT_OFF),
    "reload-icon" to fun() = FontIcon(MaterialDesignR.RELOAD),
    "code-braces-icon" to fun() = FontIcon(MaterialDesignC.CODE_BRACES),
    "github-icon" to fun() = FontIcon(MaterialDesignG.GITHUB),
    "email-icon" to fun() = FontIcon(MaterialDesignE.EMAIL),
    "twitter-icon" to fun() = FontIcon(MaterialDesignT.TWITTER),
    "contact-mail-icon" to fun() = FontIcon(MaterialDesignE.EMAIL),
    "search-icon" to fun() = FontIcon(Material2MZ.SEARCH),
    "delete-icon" to fun() = FontIcon(MaterialDesignD.DELETE),
    "delete-forever-icon" to fun() = FontIcon(MaterialDesignD.DELETE_FOREVER),
    "border-top-icon" to fun() = FontIcon(MaterialDesignB.BORDER_TOP),
    "columns-icon" to fun() = FontIcon(MaterialDesignV.VIEW_COLUMN),
    "table-icon" to fun() = FontIcon(MaterialDesignT.TABLE),
    "dock-bottom-icon" to fun() = FontIcon(MaterialDesignB.BORDER_BOTTOM),
    "translate-icon" to fun() = FontIcon(MaterialDesignT.TRANSLATE),
    "database-clock-icon" to fun() = FontIcon(MaterialDesignH.HISTORY),
    "paint-icon" to fun() = FontIcon(MaterialDesignF.FORMAT_PAINT),
    "library-icon" to fun() = FontIcon(MaterialDesignL.LIBRARY),
    "full-screen-icon" to fun() = FontIcon(MaterialDesignF.FULLSCREEN),
    "clipboard-icon" to fun() = FontIcon(MaterialDesignC.CLIPBOARD),
    "save-icon" to fun() = FontIcon(MaterialDesignC.CONTENT_SAVE),
    "pencil-icon" to fun() = FontIcon(MaterialDesignP.PENCIL),
    "modules-icon" to fun() = FontIcon(MaterialDesignV.VIEW_MODULE),
    "star-icon" to fun() = FontIcon(MaterialDesignS.STAR),
    "palette-advanced-icon" to fun() = FontIcon(MaterialDesignP.PALETTE_ADVANCED),
    "details-icon" to fun() = FontIcon(MaterialDesignD.DETAILS),
    "tune-icon" to fun() = FontIcon(MaterialDesignT.TUNE),
    "buffer-icon" to fun() = FontIcon(MaterialDesignB.BUFFER),
    "numeric-2-box-multiple" to fun() = FontIcon(MaterialDesignN.NUMERIC_2_BOX_MULTIPLE_OUTLINE),
    "rotate-icon" to fun() = FontIcon(MaterialDesignR.ROTATE_3D),
    "duplicate-icon" to fun() = FontIcon(MaterialDesignC.CONTENT_DUPLICATE),
    "excel-icon" to fun() = FontIcon(MaterialDesignF.FILE_EXCEL),
    "bold-icon" to fun() = FontIcon(MaterialDesignF.FORMAT_BOLD),
    "italic-icon" to fun() = FontIcon(MaterialDesignF.FORMAT_ITALIC),
    "strikethrough-icon" to fun() = FontIcon(MaterialDesignF.FORMAT_STRIKETHROUGH),
    "pause-icon" to fun() = FontIcon(MaterialDesignP.PAUSE),
    "stop-icon" to fun() = FontIcon(MaterialDesignS.STOP),
    "spider-icon" to fun() = FontIcon(MaterialDesignS.SPIDER_WEB)
)

fun icon(identifier: String): Text = (iconPack[identifier]?.invoke() ?: Text().apply {
    logger.error("couldn't find icon for '{}'", identifier)
}).styleClass(identifier)

