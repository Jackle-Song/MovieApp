package com.mrsworkshop.movieapp.activity

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.mrsworkshop.movieapp.R
import com.mrsworkshop.movieapp.apidata.response.MovieDetailDTO
import com.mrsworkshop.movieapp.databinding.ActivityMovieDetailsBinding
import com.mrsworkshop.movieapp.viewModel.MovieListApiData
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MovieDetailsActivity : BaseActivity() {
    private lateinit var binding: ActivityMovieDetailsBinding

    private var movieListApiData : MovieListApiData = MovieListApiData()
    private var movieDetailDTO : MovieDetailDTO = MovieDetailDTO()
    private var imdbID : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window?.statusBarColor = Color.TRANSPARENT
        setStatusBarLightTheme(false)

        imdbID = intent.getStringExtra(MovieListActivity.MOVIE_DETAIL_ID)
        retrieveMovieDetail(imdbID ?: "")
    }

    /**
     * private function
     */

    private fun initUI() {
        Glide.with(this@MovieDetailsActivity)
            .load(movieDetailDTO.Poster)
            .apply(bitmapTransform(BlurTransformation(25, 3)))
            .into(binding.imgBlurMovieImage)
        Glide.with(this@MovieDetailsActivity)
            .load(movieDetailDTO.Poster)
            .into(binding.imgCardMovieImage)

        binding.txtMovieRunTime.text = movieDetailDTO.Runtime
        val ratingOutOf10 = movieDetailDTO.imdbRating?.toFloat()
        val maxRating = 10f
        val maxStars = 5
        val convertedRating = (ratingOutOf10?.div(maxRating))?.times(maxStars)
        if (convertedRating != null) {
            binding.ratingBarMovieDetail.rating = convertedRating
        }
        binding.txtRatingsOverTen.text = String.format("%s / 10", movieDetailDTO.imdbRating)
        binding.txtTotalRatings.text = getString(R.string.movie_detail_activity_total_ratings_text, movieDetailDTO.imdbVotes)

        binding.txtMovieTitleWithYear.text = getString(R.string.movie_detail_activity_movie_title_with_year_text, movieDetailDTO.Title, movieDetailDTO.Year)
        binding.txtMovieType.text = movieDetailDTO.Genre
        binding.txtPlotSummaryContent.text = movieDetailDTO.Plot

        binding.layoutOtherRatings.removeAllViews()
        for (index in 0 until (movieDetailDTO.Ratings?.size ?: 0)) {
            val movieDetailRatingItem = movieDetailDTO.Ratings?.get(index)

            val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val otherRatingsItemView : View = layoutInflater.inflate(R.layout.item_other_ratings, null)

            val txtOtherRatingsTitle : TextView = otherRatingsItemView.findViewById(R.id.txtOtherRatingsTitle)
            val txtOtherRatingsValue : TextView = otherRatingsItemView.findViewById(R.id.txtOtherRatingsValue)

            txtOtherRatingsTitle.text = movieDetailRatingItem?.Source
            txtOtherRatingsValue.text = movieDetailRatingItem?.Value

            binding.layoutOtherRatings.addView(otherRatingsItemView)
        }
    }

    /**
     * api call
     */
    private fun retrieveMovieDetail(id : String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = movieListApiData.getMovieDetail(id)?.execute()
                withContext(Dispatchers.Main) {
                    if (response?.isSuccessful == true) {
                        val movieResponse = response.body()
                        if (movieResponse != null) {
                            movieDetailDTO = movieResponse
                            initUI()
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