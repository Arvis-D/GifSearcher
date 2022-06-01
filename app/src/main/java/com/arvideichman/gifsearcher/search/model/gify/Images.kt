package com.arvideichman.gifsearcher.search.model.gify

import com.squareup.moshi.Json

data class Images(
    @Json(name = "preview_gif") val preview: Preview,
    val original: Original
)