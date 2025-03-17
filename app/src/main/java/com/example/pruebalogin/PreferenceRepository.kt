package com.example.pruebalogin.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class PreferenceRepository(context: Context) {
    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "encrypted_prefs", // Nombre del archivo
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun isDarkModeEnabled(): Boolean =
        sharedPreferences.getBoolean("dark_mode", false)

    fun setDarkModeEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean("dark_mode", enabled).apply()
    }
}
