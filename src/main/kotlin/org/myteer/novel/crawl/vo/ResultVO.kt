package org.myteer.novel.crawl.vo

data class ResultVO<T>(
    val status: Int? = null,
    val info: String? = null,
    val data: T? = null
)