package com.example.proyectofinal.api

import com.example.proyectofinal.models.Movie
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieApiService {
    @GET("movies/")
    suspend fun getMovies(): List<Movie>

    @GET("movies/{id}")
    suspend fun getMovieById(@Path("id") id: Int): Movie
}
