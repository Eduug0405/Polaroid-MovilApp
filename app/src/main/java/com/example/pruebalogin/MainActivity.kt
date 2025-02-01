// app/src/main/java/com/example/pruebalogin/MainActivity.kt
package com.example.pruebalogin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pruebalogin.ui.HomeScreen
import com.example.pruebalogin.ui.LoginScreen
import com.example.pruebalogin.ui.theme.PruebaloginTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PruebaloginTheme {
                // Crear el NavController para gestionar la navegación entre pantallas
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Routes.LOGIN) {
                    // Pantalla de Login
                    composable(Routes.LOGIN) {
                        LoginScreen(
                            onLoginSuccess = {
                                // Navegar a la pantalla de inicio y limpiar la pila de navegación
                                navController.navigate(Routes.HOME) {
                                    popUpTo(Routes.LOGIN) { inclusive = true }
                                }
                            }
                        )
                    }
                    // Pantalla de Inicio (Home)
                    composable(Routes.HOME) {
                        HomeScreen()
                    }
                }
            }
        }
    }
}
