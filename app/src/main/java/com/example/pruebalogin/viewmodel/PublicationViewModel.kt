// app/src/main/java/com/example/pruebalogin/viewmodel/PublicationViewModel.kt
package com.example.pruebalogin.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pruebalogin.model.Publication
import com.example.pruebalogin.model.PublicationsResponse
import com.example.pruebalogin.repository.PublicationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


sealed class PublicationState {
    object Loading : PublicationState()
    data class Success(val publications: List<Publication>) : PublicationState()
    data class Error(val message: String) : PublicationState()
}

class PublicationViewModel : ViewModel() {
    private val repository = PublicationRepository()

    private val _publicationState = MutableStateFlow<PublicationState>(PublicationState.Loading)
    val publicationState: StateFlow<PublicationState> = _publicationState

    init {
        fetchPublications()
    }

    fun fetchPublications() {
        _publicationState.value = PublicationState.Loading
        viewModelScope.launch {
            try {
                val response = repository.getPublications()
                if (response.isSuccessful) {
                    response.body()?.let { publicationsResponse ->
                        _publicationState.value = PublicationState.Success(publicationsResponse.publications)
                    } ?: run {
                        _publicationState.value = PublicationState.Error("Datos vacíos")
                    }
                } else {
                    _publicationState.value = PublicationState.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _publicationState.value = PublicationState.Error(e.localizedMessage ?: "Error desconocido")
            }
        }
    }

    fun createPublication(publication: Publication) {
        viewModelScope.launch {
            try {
                val response = repository.createPublication(publication)
                if (response.isSuccessful) {
                    fetchPublications() // Refrescar la lista después de crear
                } else {
                    _publicationState.value = PublicationState.Error("Error al crear publicación: ${response.code()}")
                }
            } catch (e: Exception) {
                _publicationState.value = PublicationState.Error(e.localizedMessage ?: "Error desconocido")
            }
        }
    }

    fun updatePublication(id: Int, publication: Publication) {
        viewModelScope.launch {
            try {
                val response = repository.updatePublication(id, publication)
                if (response.isSuccessful) {
                    fetchPublications() // Refrescar la lista después de actualizar
                } else {
                    _publicationState.value = PublicationState.Error("Error al actualizar publicación: ${response.code()}")
                }
            } catch (e: Exception) {
                _publicationState.value = PublicationState.Error(e.localizedMessage ?: "Error desconocido")
            }
        }
    }

    fun deletePublication(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.deletePublication(id)
                if (response.isSuccessful) {
                    fetchPublications() // Refrescar la lista después de eliminar
                } else {
                    _publicationState.value = PublicationState.Error("Error al eliminar publicación: ${response.code()}")
                }
            } catch (e: Exception) {
                _publicationState.value = PublicationState.Error(e.localizedMessage ?: "Error desconocido")
            }
        }
    }
}
