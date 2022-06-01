package com.arvideichman.gifsearcher.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.arvideichman.gifsearcher.R
import com.arvideichman.gifsearcher.search.model.gify.Gif
import com.arvideichman.gifsearcher.search.model.gify.Pagination
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val REQUEST_ITEM_COUNT = 20

class SearchViewModel(application: Application): AndroidViewModel(application) {
    val gifResults by lazy { MutableLiveData<List<Gif>>() }
    val chosenGif by lazy { MutableLiveData<Gif?>(null) }
    val loading by lazy { MutableLiveData(false) }
    val searchBarOpened by lazy { MutableLiveData(false) }
    private var pagination: Pagination? = null
    private var currentQuery: String = ""
    val moshi: Moshi by lazy { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }
    private val giphyService: GiphyService by lazy {
        Retrofit.Builder()
            .baseUrl(application.getString(R.string.giphy_gif_api))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GiphyService::class.java)
    }
    private var currentCallJob: Job? = null

    fun newGifSearch(query: String, offset: Int = 0, delay: Long = 150, addToCurrent: Boolean = false) {
        currentCallJob?.cancel()

        if (query.isBlank()) return

        currentQuery = query

        val call = giphyService.requestGifs(
            getApplication<Application>().getString(R.string.gipfy_api_key),
            query,
            REQUEST_ITEM_COUNT,
            offset
        )

        loading.value = true
        currentCallJob = viewModelScope.launch(Dispatchers.IO) {
            delay(delay)
            val response = call.execute()
            if (response.isSuccessful) {
                response.body()?.also {
                    launch(Dispatchers.Main) {
                        if (addToCurrent) {
                            gifResults.value = gifResults.value?.plus(it.data)
                        } else {
                            gifResults.value = it.data
                        }
                        pagination = it.pagination
                        loading.value = false
                    }
                }
            }
        }
    }

    fun requestMoreItems() = pagination?.let {
        newGifSearch(currentQuery,
            it.offset + REQUEST_ITEM_COUNT,
            0,
            true)
    }
}