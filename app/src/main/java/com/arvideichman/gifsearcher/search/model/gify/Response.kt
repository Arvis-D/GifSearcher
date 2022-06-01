package com.arvideichman.gifsearcher.search.model.gify

data class Response(
    val data: List<Gif>,
    val pagination: Pagination?
    )