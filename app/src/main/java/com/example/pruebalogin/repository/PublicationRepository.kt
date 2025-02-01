// app/src/main/java/com/example/pruebalogin/repository/PublicationRepository.kt
package com.example.pruebalogin.repository

import com.example.pruebalogin.model.Publication
import com.example.pruebalogin.model.PublicationsResponse
import com.example.pruebalogin.network.RetrofitClient
import retrofit2.Response

class PublicationRepository {
    /**
     * Obtiene todas las publicaciones desde la API.
     *
     * @return Response<List<Publication>> - Respuesta de la API con una lista de publicaciones.
     */
    suspend fun getPublications(): Response<PublicationsResponse> {
        return RetrofitClient.apiService.getPublications()
    }

    /**
     * Obtiene una publicación específica por su ID desde la API.
     *
     * @param id Int - ID de la publican a obtener.
     * @return Response<Publication> - Respuesta de la API con la publicación solicitada.
     */
    suspend fun getPublicationById(id: Int): Response<Publication> {
        return RetrofitClient.apiService.getPublicationById(id)
    }

    /**
     * Crea una nueva publicación en la API.
     *
     * @param publication Publication - Objeto de la publicación a crear.
     * @return Response<Publication> - Respuesta de la API con la publicación creada.
     */
    suspend fun createPublication(publication: Publication): Response<Publication> {
        return RetrofitClient.apiService.createPublication(publication)
    }

    /**
     * Actualiza una publicación existente en la API.
     *
     * @param id Int - ID de la publicación a actualizar.
     * @param publication Publication - Objeto de la publicación con los nuevos datos.
     * @return Response<Publication> - Respuesta de la API con la publicación actualizada.
     */
    suspend fun updatePublication(id: Int, publication: Publication): Response<Publication> {
        return RetrofitClient.apiService.updatePublication(id, publication)
    }

    /**
     * Elimina una publicación por su ID en la API.
     *
     * @param id Int - ID de la publicación a eliminar.
     * @return Response<Void> - Respuesta de la API indicando el resultado de la operación.
     */
    suspend fun deletePublication(id: Int): Response<Void> {
        return RetrofitClient.apiService.deletePublication(id)
    }
}
