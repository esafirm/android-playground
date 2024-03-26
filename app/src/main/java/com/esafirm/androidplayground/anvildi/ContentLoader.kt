package com.esafirm.androidplayground.anvildi

import com.esafirm.androidplayground.libs.Logger

class ContentLoader(
    private val contentId: Int
) {
    fun loadContent() {
        Logger.log("Loading content with id: $contentId")
    }
}

interface ContentIdProvider {
    val contentId: Int
}
