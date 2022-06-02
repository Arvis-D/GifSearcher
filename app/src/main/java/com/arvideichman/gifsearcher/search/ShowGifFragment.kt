package com.arvideichman.gifsearcher.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.arvideichman.gifsearcher.databinding.FragmentShowGifBinding
import com.bumptech.glide.Glide

class ShowGifFragment : Fragment() {
    private lateinit var b: FragmentShowGifBinding
    private val model by lazy { ViewModelProvider(requireActivity())[SearchViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentShowGifBinding.inflate(layoutInflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.chosenGif.observe(this) {
            if (it == null) return@observe
            Glide.with(this)
                .load(it.images.original.url)
                .fitCenter()
                .into(b.gifImage)

            b.gifTitle.text = if (it.title.isBlank()) "Untitled" else it.title
        }
    }
}