package com.example.proyectofinal.ui.Screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.proyectofinal.api.RetrofitClient
import com.example.proyectofinal.models.Movie

@Composable
fun HomeScreen(navController: NavHostController, context: Context) {
    var movies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    // Llamada a la API para obtener las películas
    LaunchedEffect(Unit) {
        try {
            isLoading = true
            movies = RetrofitClient.movieApiService.getMovies()
        } catch (e: Exception) {
            errorMessage = "Error al cargar las películas: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    // Interfaz de usuario
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                Text(text = "Cargando...", color = Color.Gray)
            }
            errorMessage.isNotEmpty() -> {
                Text(text = errorMessage, color = Color.Red)
            }
            movies.isNotEmpty() -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2), // Configuración para 2 columnas
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(movies.size) { index ->
                        val movie = movies[index]
                        MovieCard(movie = movie, navController = navController)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
            else -> {
                Text(text = "No hay películas disponibles", color = Color.Gray)
            }
        }
    }
}

@Composable
fun MovieCard(movie: Movie, navController: NavHostController) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate("detail/${movie.id}")
            },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = movie.imageUrl,
                contentDescription = movie.name,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = movie.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}
