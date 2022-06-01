package com.arvideichman.gifsearcher.search.recycler

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.arvideichman.gifsearcher.R
import com.arvideichman.gifsearcher.databinding.ItemSearchBinding
import com.arvideichman.gifsearcher.search.SearchViewModel
import com.arvideichman.gifsearcher.search.model.gify.Gif
import com.bumptech.glide.Glide

class ResultViewHolder(
    private val b: ItemSearchBinding,
    private val context: Context,
    private val model: SearchViewModel
    ):
    RecyclerView.ViewHolder(b.root) {
    private var gif: Gif? = null

    fun interface OnItemSelected {
        fun onItemSelected()
    }
    var listener: OnItemSelected? = null

    init {
        b.root.setOnClickListener {
            if (gif == null) return@setOnClickListener

            listener?.onItemSelected()
            model.chosenGif.value = gif
        }
    }

    fun set(gif: Gif) {
        this.gif = gif

        Glide.with(b.root)
            .load(gif.images.preview.url)
            .centerCrop()
            .into(b.resultPreview)

        b.resultTitle.text = if (gif.title.isBlank()) context.getString(R.string.untitled)
                             else gif.title
    }
}