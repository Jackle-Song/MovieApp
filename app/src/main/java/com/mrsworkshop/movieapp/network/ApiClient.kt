package com.mrsworkshop.movieapp.network

import com.mrsworkshop.movieapp.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    fun retrofitService(): ApiInterface {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_MOVIE)
            .client(execOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiInterface::class.java)
    }

    private fun execOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .build()
    }
}