package com.mrsworkshop.movieapp.model

import androidx.room.Entity

data class UserCredentialVO (
    val id : Int? = null,
    val username : String? = null,
    val email : String? = null,
    val password : String? = null,
)