// app/src/main/java/com/example/pruebalogin/model/PublicationsResponse.kt
package com.example.pruebalogin.model

import com.google.gson.annotations.SerializedName

data class PublicationsResponse(
    val success: Boolean,
    val publications: List<Publication>
)
