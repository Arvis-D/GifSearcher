package com.arvideichman.gifsearcher.search.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arvideichman.gifsearcher.databinding.ItemSearchBinding
import com.arvideichman.gifsearcher.search.SearchViewModel
import com.arvideichman.gifsearcher.search.model.gify.Gif

class ResultAdapter(
    private val inflater: LayoutInflater,
    private val context: Context,
    private val model: SearchViewModel
    ):
    RecyclerView.Adapter<ResultViewHolder>() {
    private var results = listOf<Gif>()
    var searchItemSelectedListener: ResultViewHolder.OnItemSelected? = null

    private class SearchScrollListener(
        private val model: SearchViewModel,
    ): RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            if (dy < 0 || model.loading.value == true) return

            (recyclerView.layoutManager as LinearLayoutManager?)?.let {
                val visibleItemCount = it.childCount
                val totalItemCount = it.itemCount
                val pastVisibleItems = it.findFirstVisibleItemPosition()

                if ((visibleItemCount + pastVisibleItems) >= (totalItemCount - 10)) {
                    model.requestMoreItems()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ItemSearchBinding.inflate(
            inflater,
            parent,
            false
        )

        val holder = ResultViewHolder(binding, context, model)
        holder.listener = searchItemSelectedListener

        return holder
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(SearchScrollListener(model))
    }

    fun setSuggestions(newResults: List<Gif>?) = newResults?.let { it ->
        val diffUtil = ResultDiffUtil(results, it)
        val diffUtilResult = DiffUtil.calculateDiff(diffUtil)
        diffUtilResult.dispatchUpdatesTo(this)

        results = newResults
    } ?: Unit

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.set(results[position])
    }

    override fun getItemCount() = results.size
}