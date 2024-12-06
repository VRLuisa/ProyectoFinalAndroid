package com.example.proyectofinal.ui.Screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.proyectofinal.R
import com.example.proyectofinal.data.UserPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LoginScreen(navController: NavHostController, context: Context) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val userPreferences = UserPreferences(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Imagen de Login
        Image(
            painter = painterResource(id = R.drawable.login),
            contentDescription = "Imagen de inicio de sesión",
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Bienvenido de vuelta", fontSize = 24.sp)
        Text(text = "Inicia sesión en tu cuenta", fontSize = 16.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de correo electrónico
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para iniciar sesión
        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val savedEmail = userPreferences.email.firstOrNull() ?: ""
                        val savedPassword = userPreferences.password.firstOrNull() ?: ""

                        if (email == savedEmail && password == savedPassword) {
                            withContext(Dispatchers.Main) {
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                errorMessage = "Credenciales incorrectas."
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            errorMessage = "Error al validar las credenciales."
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Iniciar Sesión")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Mensaje de error
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Texto clicable para recuperar contraseña
        Text(
            text = "¿Olvidaste tu contraseña?",
            color = Color.Blue,
            modifier = Modifier.clickable {
                // Lógica para recuperación de contraseña
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Texto para redirigir al registro
        Text(text = "¿No tienes cuenta? Regístrate abajo")

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para ir a la pantalla de registro
        Button(
            onClick = {
                navController.navigate("register")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Registrarse")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Iconos de redes sociales
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(id = R.drawable.facebook),
                contentDescription = "Iniciar sesión con Facebook",
                modifier = Modifier
                    .size(64.dp)
                    .clickable {
                        // Lógica para iniciar sesión con Facebook
                    }
            )
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "Iniciar sesión con Google",
                modifier = Modifier
                    .size(64.dp)
                    .clickable {
                        // Lógica para iniciar sesión con Google
                    }
            )
            Image(
                painter = painterResource(id = R.drawable.twitter),
                contentDescription = "Iniciar sesión con Twitter",
                modifier = Modifier
                    .size(64.dp)
                    .clickable {
                        // Lógica para iniciar sesión con Twitter
                    }
            )
        }
    }
}
