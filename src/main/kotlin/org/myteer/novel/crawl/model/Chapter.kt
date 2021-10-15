package org.myteer.novel.crawl.model

import com.fasterxml.jackson.annotation.JsonProperty

class Chapter {
    @JsonProperty("id")
    var bookId: String? = null

    @JsonProperty("name")
    var bookName: String? = null

    @JsonProperty("cid")
    var id: String? = null

    @JsonProperty("cname")
    var name: String? = null

    @JsonProperty("pid")
    var previousId: String? = null

    @JsonProperty("nid")
    var nextId: String? = null

    var hasContent: Int? = null

    var content: String? = null

    var volumeIndex: Int? = null

    var volumeName: String? = null
}