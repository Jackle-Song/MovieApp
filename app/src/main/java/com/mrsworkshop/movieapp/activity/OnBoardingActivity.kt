package com.mrsworkshop.movieapp.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.mrsworkshop.movieapp.R
import com.mrsworkshop.movieapp.databinding.ActivityOnboardingBinding
import com.mrsworkshop.movieapp.mySQLite.MovieListDatabaseHandler
import com.mrsworkshop.movieapp.viewModel.MovieListApiData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OnBoardingActivity : BaseActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var mAuth : FirebaseAuth

    private var movieListApiData : MovieListApiData = MovieListApiData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        retrieveMovieList()
    }

    private fun initUI() {
        Glide.with(this@OnBoardingActivity)
            .load(R.drawable.onboarding_load)
            .into(binding.imgOnboarding)
    }

    private fun getCurrentUser() {
        mAuth = FirebaseAuth.getInstance()

        if (mAuth.currentUser != null) {
            val intent = Intent(this@OnBoardingActivity, MovieListActivity::class.java)
            startActivity(intent)
        }
        else {
            val intent = Intent(this@OnBoardingActivity, AuthenticationActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * api call
     */

    private fun retrieveMovieList() {
        if (isNetworkAvailable(this@OnBoardingActivity)) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = movieListApiData.getMovieList()?.execute()
                    withContext(Dispatchers.Main) {
                        if (response?.isSuccessful == true) {
                            val movieResponse = response.body()
                            if (movieResponse != null) {
                                val databaseHandler = MovieListDatabaseHandler(this@OnBoardingActivity)
                                for (index in 0 until (movieResponse.Search?.size ?: 0)) {
                                    val movieItem = movieResponse.Search?.get(index)

                                    val existingMovieList = databaseHandler.retrieveLocalMovieListItem()

                                    val existingMovie = existingMovieList.find { it.imdbID == movieItem?.imdbID }

                                    if (existingMovie != null) {
                                        if (movieItem != null) {
                                            databaseHandler.updateLocalMovieListDetailItem(movieItem.imdbID ?: "", movieItem)
                                        }
                                    } else {
                                        if (movieItem != null) {
                                            databaseHandler.addNewMovieListDetailItem(movieItem.imdbID ?: "", movieItem)
                                        }
                                    }
                                }
                                getCurrentUser()
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
        else {
            Handler(Looper.getMainLooper()).postDelayed({
                getCurrentUser()
            }, 1000)
        }
    }
}