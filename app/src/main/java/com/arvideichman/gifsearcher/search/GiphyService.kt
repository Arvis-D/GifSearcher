package com.arvideichman.gifsearcher.search

import com.arvideichman.gifsearcher.search.model.gify.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyService {
    @GET("search")
    fun requestGifs(
        @Query("api_key") key: String,
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Call<Response>
}