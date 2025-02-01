// app/src/main/java/com/example/pruebalogin/model/Publication.kt
package com.example.pruebalogin.model

import com.google.gson.annotations.SerializedName

data class Publication(
    val id: Int,
    val title: String,
    val description: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)
