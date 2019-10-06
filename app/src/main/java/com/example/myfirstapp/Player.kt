package com.example.myfirstapp

import com.squareup.picasso.RequestCreator

data class Player (
    val id: Int,
    val name: String,
    val position: String,
    val picture: RequestCreator
)