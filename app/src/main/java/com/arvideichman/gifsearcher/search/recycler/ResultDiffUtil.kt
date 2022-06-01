package com.arvideichman.gifsearcher.search.recycler

import androidx.recyclerview.widget.DiffUtil
import com.arvideichman.gifsearcher.search.model.gify.Gif

class ResultDiffUtil(
    private val old: List<Gif>,
    private val new: List<Gif>
): DiffUtil.Callback() {
    override fun getOldListSize() = old.size
    override fun getNewListSize() = new.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        old[oldItemPosition].id == new[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = old[oldItemPosition]
        val newItem = new[newItemPosition]
        return  oldItem.title == newItem.title &&
                oldItem.images.original == newItem.images.original &&
                oldItem.images.preview == newItem.images.preview
    }
}