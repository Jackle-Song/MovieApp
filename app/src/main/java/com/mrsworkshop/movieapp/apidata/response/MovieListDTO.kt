package com.mrsworkshop.movieapp.apidata.response

data class MovieListDTO (
    var Search : MutableList<MovieListDetails>? = mutableListOf()
) : DTO()

data class MovieListDetails (
    var Title : String? = null,
    var Year : String? = null,
    var imdbID : String? = null,
    var Type : String? = null,
    var Poster : String? = null,
)