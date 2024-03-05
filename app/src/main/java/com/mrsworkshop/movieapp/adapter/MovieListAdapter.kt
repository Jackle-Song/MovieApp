package com.mrsworkshop.movieapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import com.mrsworkshop.movieapp.R
import com.mrsworkshop.movieapp.apidata.response.MovieListDetails

class MovieListAdapter(
    private var mContext : Context,
    private var movieDetailsList : MutableList<MovieListDetails>?,
    private var filteredMovieDetailsList : MutableList<MovieListDetails>?,
    private var mListener : MovieListInterface,
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        filteredMovieDetailsList = movieDetailsList
    }

    interface MovieListInterface {
        fun viewMovieDetails(imdbID : String?)
    }

    class MovieDetailsViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val imgMovieImage : ImageView = itemView.findViewById(R.id.imgMovieImage)
        val txtMovieTitle : TextView = itemView.findViewById(R.id.txtMovieTitle)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview_movie_list, parent, false)
        return MovieDetailsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredMovieDetailsList?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movieDetailItem = filteredMovieDetailsList?.get(position)
        val movieDetailsViewHolder = holder as MovieDetailsViewHolder

        Glide.with(mContext)
            .load(movieDetailItem?.Poster)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(movieDetailsViewHolder.imgMovieImage)

        movieDetailsViewHolder.txtMovieTitle.text = movieDetailItem?.Title

        movieDetailsViewHolder.imgMovieImage.setOnClickListener {
            mListener.viewMovieDetails(movieDetailItem?.imdbID)
        }
    }

    fun updateMovieList(updatedMovieList : MutableList<MovieListDetails>?) {
        movieDetailsList = updatedMovieList ?: mutableListOf()
        filteredMovieDetailsList = movieDetailsList?.toMutableList()
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        filteredMovieDetailsList?.clear()

        if (query.isEmpty()) {
            filteredMovieDetailsList?.addAll(movieDetailsList ?: mutableListOf())
        } else {
            movieDetailsList?.let { originalList ->
                val filtered = originalList.filter { movies ->
                    movies.Title?.contains(query, true) == true
                }
                filteredMovieDetailsList?.addAll(filtered)
            }
        }

        notifyDataSetChanged()
    }
}