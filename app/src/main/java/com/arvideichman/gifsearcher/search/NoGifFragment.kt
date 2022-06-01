package com.arvideichman.gifsearcher.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arvideichman.gifsearcher.databinding.FragmentNoGifBinding

class NoGifFragment : Fragment() {
    private lateinit var b: FragmentNoGifBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentNoGifBinding.inflate(inflater, container, false)

        return b.root
    }
}