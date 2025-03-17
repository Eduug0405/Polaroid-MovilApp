package com.example.pruebalogin.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class EncryptedPrefsHelper(context: Context) {

    companion object {
        private const val PREFS_FILE = "secure_prefs"
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_LAST_ACCESS = "last_access"
        private const val KEY_LAST_LOCATION = "last_location"

        // NUEVO: clave para el volumen
        private const val KEY_VOLUME = "volume_level"
    }

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val securePrefs = EncryptedSharedPreferences.create(
        context,
        PREFS_FILE,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    // -- Modo oscuro
    fun isDarkModeEnabled(): Boolean {
        return securePrefs.getBoolean(KEY_DARK_MODE, false)
    }

    fun setDarkModeEnabled(enabled: Boolean) {
        securePrefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply()
    }

    // -- Último acceso
    fun getLastAccess(): String {
        return securePrefs.getString(KEY_LAST_ACCESS, "") ?: ""
    }

    fun setLastAccess(value: String) {
        securePrefs.edit().putString(KEY_LAST_ACCESS, value).apply()
    }

    // -- Última ubicación
    fun getLastLocation(): String {
        return securePrefs.getString(KEY_LAST_LOCATION, "Ubicación desconocida") ?: "Ubicación desconocida"
    }

    fun setLastLocation(value: String) {
        securePrefs.edit().putString(KEY_LAST_LOCATION, value).apply()
    }

    // -- NUEVO: Volumen
    fun getVolumeLevel(): Float {
        // Valor por defecto 0.5f (50% de volumen)
        return securePrefs.getFloat(KEY_VOLUME, 0.5f)
    }

    fun setVolumeLevel(value: Float) {
        securePrefs.edit().putFloat(KEY_VOLUME, value).apply()
    }
}
