package com.example.pruebalogin.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.lifecycle.compose.collectAsStateWithLifecycle

import com.example.pruebalogin.viewmodel.StudentViewModel

@Composable
fun StudentScreen(studentViewModel: StudentViewModel) {
    // Observamos los StateFlows:
    val isDarkMode by studentViewModel.isDarkMode.collectAsStateWithLifecycle()
    val students by studentViewModel.students.collectAsStateWithLifecycle()
    val lastAccess by studentViewModel.lastAccess.collectAsStateWithLifecycle()
    val lastLocation by studentViewModel.lastLocation.collectAsStateWithLifecycle()
    val volume by studentViewModel.volume.collectAsStateWithLifecycle() // 0f..1f

    var newStudentName by remember { mutableStateOf("") }

    // Actualiza la hora de último acceso al entrar
    LaunchedEffect(Unit) {
        studentViewModel.updateLastAccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Mostrar la última hora de acceso
        Text(
            text = "Último acceso: $lastAccess",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Mostrar la última ubicación
        Text(
            text = "Última ubicación en que se posteo una foto: $lastLocation",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Toggle para modo oscuro
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = if (isDarkMode) "Modo Oscuro: ON" else "Modo Oscuro: OFF")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = isDarkMode,
                onCheckedChange = { studentViewModel.toggleDarkMode() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // NUEVO: Control de volumen para notificaciones
        Text(
            text = "Volumen de las notificaciones al recibir un post: ${(volume * 100).toInt()}%",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Slider(
            value = volume,
            onValueChange = { studentViewModel.updateVolume(it) },
            valueRange = 0f..1f
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para quitar todos los temas
        Button(onClick = { studentViewModel.removeAllStudents() }) {
            Text("Quitar todos los temas")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para agregar un nuevo "tema"
        OutlinedTextField(
            value = newStudentName,
            onValueChange = { newStudentName = it },
            label = { Text("Agregar tema de interés") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (newStudentName.isNotBlank()) {
                    studentViewModel.addStudent(newStudentName)
                    newStudentName = ""
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Agregar Tema")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de temas
        if (students.isEmpty()) {
            Text("No hay temas cargados.", style = MaterialTheme.typography.bodyLarge)
        } else {
            LazyColumn {
                items(students) { student ->
                    Text(
                        text = student,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}
