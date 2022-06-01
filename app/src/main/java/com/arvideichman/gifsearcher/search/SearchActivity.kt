package com.arvideichman.gifsearcher.search

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arvideichman.gifsearcher.R
import com.arvideichman.gifsearcher.databinding.ActivitySearchBinding
import com.arvideichman.gifsearcher.search.model.gify.Gif
import com.arvideichman.gifsearcher.search.recycler.ResultAdapter
import com.arvideichman.gifsearcher.search.recycler.ResultViewHolder
import com.mancj.materialsearchbar.MaterialSearchBar

const val LAST_GIF = "LAST_GIF"

class SearchActivity : AppCompatActivity() {
    private lateinit var b: ActivitySearchBinding
    private val model by lazy { ViewModelProvider(this)[SearchViewModel::class.java] }
    private val resultAdapter by lazy {
        ResultAdapter(layoutInflater, this, model)
    }
    private class SearchQueryChangeWatcher(
        private val model: SearchViewModel
        ): TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
        override fun afterTextChanged(s: Editable?) { }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            model.newGifSearch(s.toString(), 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(b.root)

        tryGetLastGifFromPreferences()
        setUpSearchBar()

        model.chosenGif.observe(this) { saveLastGif(it) }
        model.gifResults.observe(this) { resultAdapter.setSuggestions(it) }
        model.searchBarOpened.observe(this) { opened ->
            (if (opened) View.VISIBLE else View.GONE).let {
                b.searchResultRecycler.visibility = it
                b.resultOverlay.visibility = it
            }
        }
    }

    private fun setUpSearchBar() {
        b.searchBar.hideSuggestionsList()
        b.searchResultRecycler.apply {
            adapter = this@SearchActivity.resultAdapter
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false
            )
        }

        resultAdapter.searchItemSelectedListener = ResultViewHolder.OnItemSelected {
            b.searchBar.closeSearch()
            setFragment(ShowGifFragment())
        }

        b.searchBar.setOnSearchActionListener(object: MaterialSearchBar.OnSearchActionListener {
            override fun onSearchStateChanged(enabled: Boolean) {
                model.searchBarOpened.value = enabled
            }
            override fun onSearchConfirmed(text: CharSequence?) {}
            override fun onButtonClicked(buttonCode: Int) {}
        })
        b.searchBar.addTextChangeListener(SearchQueryChangeWatcher(model))
    }

    private fun tryGetLastGifFromPreferences() = getPreferences(Context.MODE_PRIVATE).let {
        val lastGifJson = it.getString(LAST_GIF, null)

        val fragment: Fragment =
        if (lastGifJson == null) {
            NoGifFragment()
        } else {
            val gif = model.moshi.adapter(Gif::class.java).fromJson(lastGifJson) ?: return
            model.chosenGif.value = gif
            ShowGifFragment()
        }

        setFragment(fragment)
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.gif_fragment, fragment)
            setReorderingAllowed(true)
        }
    }

    private fun saveLastGif(gif: Gif?) {
        if (gif == null) return

        getPreferences(Context.MODE_PRIVATE).edit().also {
            it.putString(LAST_GIF, model.moshi.adapter(Gif::class.java).toJson(gif))
            it.apply()
        }
    }
}