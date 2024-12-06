package com.example.proyectofinal.ui.Screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.proyectofinal.api.RetrofitClient
import com.example.proyectofinal.models.Movie

@Composable
fun DetailScreen(navController: NavHostController, movieId: Int) {
    var movie by remember { mutableStateOf<Movie?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    // Llamada a la API
    LaunchedEffect(movieId) {
        try {
            isLoading = true
            val response = RetrofitClient.movieApiService.getMovieById(movieId)

            if (response != null) {
                movie = response
                Log.d("API Response", movie.toString()) // Debugging para verificar los datos
            } else {
                errorMessage = "La película no fue encontrada o el servidor devolvió una respuesta vacía."
                Log.e("API Error", "Respuesta vacía de la API.")
            }
        } catch (e: Exception) {
            errorMessage = "Error al cargar los detalles: ${e.message}"
            Log.e("API Error", e.message ?: "Error desconocido")
        } finally {
            isLoading = false
        }
    }

    // UI
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(color = Color.Red)
            }
            errorMessage.isNotEmpty() -> {
                Text(text = errorMessage, color = Color.Red)
            }
            movie != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Mostrar imagen
                    AsyncImage(
                        model = movie?.imageUrl,
                        contentDescription = movie?.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Título
                    Text(
                        text = movie?.name ?: "Nombre no disponible",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Género
                    Text(
                        text = "Género: ${movie?.genre ?: "No disponible"}",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Duración
                    Text(
                        text = "Duración: ${movie?.duration ?: "No disponible"} min",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Descripción
                    Text(
                        text = movie?.description ?: "Descripción no disponible",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}
