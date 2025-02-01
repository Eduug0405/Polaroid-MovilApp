// app/src/main/java/com/example/pruebalogin/network/ApiService.kt
package com.example.pruebalogin.network

import com.example.pruebalogin.model.LoginRequest
import com.example.pruebalogin.model.LoginResponse
import com.example.pruebalogin.model.Publication
import com.example.pruebalogin.model.PublicationsResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Endpoint de Login
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    // Obtener todas las publicaciones
    @GET("publications")
    suspend fun getPublications(): Response<PublicationsResponse>

    // Obtener una publicación por ID
    @GET("publications/{id}")
    suspend fun getPublicationById(@Path("id") id: Int): Response<Publication>

    // Crear una nueva publicación
    @POST("publications")
    suspend fun createPublication(@Body publication: Publication): Response<Publication>

    // Actualizar una publicación existente
    @PUT("publications/{id}")
    suspend fun updatePublication(
        @Path("id") id: Int,
        @Body publication: Publication
    ): Response<Publication>

    // Eliminar una publicación
    @DELETE("publications/{id}")
    suspend fun deletePublication(@Path("id") id: Int): Response<Void>
}
