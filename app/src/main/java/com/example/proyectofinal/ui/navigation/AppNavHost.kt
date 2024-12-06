package com.example.proyectofinal.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinal.ui.Screens.DetailScreen
import com.example.proyectofinal.ui.Screens.HomeScreen
import com.example.proyectofinal.ui.Screens.LoginScreen
import com.example.proyectofinal.ui.Screens.RegisterScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    context: Context
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                navController = navController,
                context = context
            )
        }
        composable("register") {
            RegisterScreen(
                navController = navController,
                context = context
            )
        }
        composable("home") {
            HomeScreen(
                navController = navController,
                context = context
            )
        }
        composable("detail/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
            if (movieId != null) {
                DetailScreen(
                    navController = navController,
                    movieId = movieId
                )
            } else {
                navController.popBackStack() // Volver a la pantalla anterior si movieId no es válido
            }
        }


    }
}
