package com.mrsworkshop.movieapp.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.GridLayoutManager
import com.mrsworkshop.movieapp.adapter.MovieListAdapter
import com.mrsworkshop.movieapp.apidata.response.MovieListDetails
import com.mrsworkshop.movieapp.databinding.ActivityMovieListBinding
import com.mrsworkshop.movieapp.viewModel.MovieListApiData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieListActivity : BaseActivity(), MovieListAdapter.MovieListInterface {

    companion object {
        const val MOVIE_DETAIL_ID = "movieDetailID"
    }

    private lateinit var binding: ActivityMovieListBinding
    private lateinit var movieListAdapter: MovieListAdapter

    private var movieListApiData : MovieListApiData = MovieListApiData()
    private var movieDetailsList : MutableList<MovieListDetails>? = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        retrieveMovieList()
        setupComponentListener()
    }

    override fun viewMovieDetails(imdbID: String?) {
        val intent = Intent(this@MovieListActivity, MovieDetailsActivity::class.java)
        intent.putExtra(MOVIE_DETAIL_ID, imdbID)
        startActivity(intent)
    }

    /**
     * private function
     */

    private fun initUI() {
        movieListAdapter = MovieListAdapter(this@MovieListActivity, movieDetailsList, movieDetailsList, this@MovieListActivity)
        binding.recyclerviewMovieList.layoutManager = GridLayoutManager(this@MovieListActivity, 2)
        binding.recyclerviewMovieList.adapter = movieListAdapter
    }

    private fun setupComponentListener() {
        binding.etEditTextSearchMovie.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                movieListAdapter.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // Not needed
            }
        })
    }

    /**
     * api call
     */

    private fun retrieveMovieList() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = movieListApiData.getMovieList()?.execute()
                withContext(Dispatchers.Main) {
                    if (response?.isSuccessful == true) {
                        val movieResponse = response.body()
                        if (movieResponse != null) {
                            movieDetailsList = movieResponse.Search ?: mutableListOf()
                            movieListAdapter.updateMovieList(movieDetailsList)
                        }
                    }
                    else {
                        // Handle unsuccessful response
                    }
                }
            }
            catch (e: Exception) {
                // Handle network errors
            }
        }
    }
}