package org.myteer.novel.gui.utils

fun java.awt.Color.toFXColor(): javafx.scene.paint.Color {
    return javafx.scene.paint.Color.rgb(red, green, blue, alpha / 255.0)
}

fun javafx.scene.paint.Color.toAWTColor(): java.awt.Color {
    return java.awt.Color(red.toFloat(), green.toFloat(), blue.toFloat(), opacity.toFloat())
}