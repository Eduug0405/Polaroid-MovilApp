package com.example.pruebalogin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pruebalogin.data.EncryptedPrefsHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ThemeViewModel(application: Application) : AndroidViewModel(application) {

    private val prefsHelper = EncryptedPrefsHelper(application.applicationContext)

    private val _isDarkTheme = MutableStateFlow(prefsHelper.isDarkModeEnabled())
    val isDarkTheme = _isDarkTheme.asStateFlow()

    fun toggleDarkTheme() {
        val newValue = !_isDarkTheme.value
        _isDarkTheme.value = newValue


        viewModelScope.launch {
            prefsHelper.setDarkModeEnabled(newValue)
        }
    }
}
