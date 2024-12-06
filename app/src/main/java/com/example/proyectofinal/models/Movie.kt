package com.example.proyectofinal.models

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("genre")
    val genre: String,

    @SerializedName("duration")
    val duration: Int,

    @SerializedName("image_url")
    val imageUrl: String? // Cambiado a opcional para evitar problemas si la URL es nula
)
