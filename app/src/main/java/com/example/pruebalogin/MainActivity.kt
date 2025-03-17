package com.example.pruebalogin

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pruebalogin.ui.HomeScreen
import com.example.pruebalogin.ui.LoginScreen
import com.example.pruebalogin.ui.StudentScreen
import com.example.pruebalogin.ui.theme.PruebaloginTheme
import com.example.pruebalogin.viewmodel.StudentViewModel
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        createNotificationChannelIfNeeded()


        retrieveFcmToken()

        setContent {

            val studentViewModel: StudentViewModel = viewModel()


            val isDarkModeState = studentViewModel.isDarkMode.collectAsState(initial = false)

            PruebaloginTheme(darkTheme = isDarkModeState.value) {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Routes.LOGIN) {
                    composable(Routes.LOGIN) {
                        LoginScreen(
                            onLoginSuccess = {
                                navController.navigate(Routes.HOME) {
                                    popUpTo(Routes.LOGIN) { inclusive = true }
                                }
                            }
                        )
                    }
                    composable(Routes.HOME) {
                        HomeScreen(
                            onStudentScreen = { navController.navigate(Routes.STUDENT) }
                        )
                    }
                    composable(Routes.STUDENT) {

                        StudentScreen(studentViewModel = studentViewModel)
                    }
                }
            }
        }
    }

    private fun createNotificationChannelIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = "Canal por defecto"
            val channelDescription = "Canal para notificaciones de la app"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun retrieveFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM_TOKEN", "Token -> $token")
                Toast.makeText(this, "FCM Token: $token", Toast.LENGTH_LONG).show()
            } else {
                Log.w("FCM_TOKEN", "Error al obtener el token", task.exception)
            }
        }
    }
}
