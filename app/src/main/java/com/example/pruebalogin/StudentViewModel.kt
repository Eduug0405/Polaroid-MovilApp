package com.example.pruebalogin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pruebalogin.data.EncryptedPrefsHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StudentViewModel(application: Application) : AndroidViewModel(application) {

    private val prefsManager = EncryptedPrefsHelper(application.applicationContext)

    private val _isDarkMode = MutableStateFlow(prefsManager.isDarkModeEnabled())
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    fun toggleDarkMode() {
        val newValue = !_isDarkMode.value
        _isDarkMode.value = newValue
        // Guardar en EncryptedSharedPreferences
        viewModelScope.launch {
            prefsManager.setDarkModeEnabled(newValue)
        }
    }

    private val _students = MutableStateFlow(listOf<String>())
    val students: StateFlow<List<String>> = _students.asStateFlow()

    fun addStudent(name: String) {
        val newList = _students.value.toMutableList().apply { add(name) }
        _students.value = newList
    }

    fun removeAllStudents() {
        _students.value = emptyList()
    }


    private val _lastAccess = MutableStateFlow(prefsManager.getLastAccess())
    val lastAccess: StateFlow<String> = _lastAccess.asStateFlow()

    fun updateLastAccess() {
        val currentDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        _lastAccess.value = currentDateTime
        prefsManager.setLastAccess(currentDateTime)
    }

    // -------------------------------------
    private val _lastLocation = MutableStateFlow(prefsManager.getLastLocation())
    val lastLocation: StateFlow<String> = _lastLocation.asStateFlow()

    fun updateLastLocation(locationString: String) {
        _lastLocation.value = locationString
        prefsManager.setLastLocation(locationString)
    }


    private val _volume = MutableStateFlow(prefsManager.getVolumeLevel())  // 0f..1f
    val volume: StateFlow<Float> = _volume.asStateFlow()

    fun updateVolume(newValue: Float) {
        _volume.value = newValue
        viewModelScope.launch {
            prefsManager.setVolumeLevel(newValue)
        }
    }
}
