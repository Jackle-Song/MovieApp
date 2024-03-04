package com.mrsworkshop.movieapp.network

import com.mrsworkshop.movieapp.apidata.response.MovieListDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("/")
    fun getMovieList(
        @Query("s") searchQuery: String?,
        @Query("type") type: String?,
        @Query("apiKey") apiKey: String?
    ): Call<MovieListDTO>?
}