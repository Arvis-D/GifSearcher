package com.arvideichman.gifsearcher.search.model.gify

import com.squareup.moshi.Json

data class Pagination (
    val count: Int,
    val offset: Int,
    @Json(name = "total_count") val total: Int
)