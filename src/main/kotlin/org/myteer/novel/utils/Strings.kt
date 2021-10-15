package org.myteer.novel.utils

fun String?.nullIfBlank() = this?.takeIf { it.isNotBlank() }