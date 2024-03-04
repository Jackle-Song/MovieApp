package com.mrsworkshop.movieapp.viewModel

import android.content.Context
import com.google.gson.GsonBuilder
import com.mrsworkshop.movieapp.apidata.response.MovieListDTO
import com.mrsworkshop.movieapp.network.ApiClient
import com.mrsworkshop.movieapp.network.ApiInterface
import com.mrsworkshop.movieapp.utils.Constants
import retrofit2.Call

class MovieListApiData {
    private var apiInterface : ApiInterface? = null
    lateinit var mContext : Context
    private val gson = GsonBuilder().setPrettyPrinting().create()

    init {
        apiInterface = ApiClient.retrofitService()
    }

    fun getMovieList(): Call<MovieListDTO>? {
        return apiInterface?.getMovieList(Constants.MOVIE_SELECTION, Constants.MOVIE_TYPE, Constants.API_KEY)
    }
}