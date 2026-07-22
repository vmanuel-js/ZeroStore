package com.example.zero.pages.colecciones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zero.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ColeccionesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<ColeccionesUIState>(ColeccionesUIState.Loading)
    val uiState: StateFlow<ColeccionesUIState> = _uiState.asStateFlow()

    fun fetchCategorias() {
        viewModelScope.launch {
            _uiState.value = ColeccionesUIState.Loading
            try {
                val response = RetrofitClient.coleccionesService.getCategorias()
                _uiState.value = ColeccionesUIState.Success(response.categorias)
            } catch (e: Exception) {
                _uiState.value = ColeccionesUIState.Error("Error al cargar colecciones: ${e.localizedMessage}")
            }
        }
    }

    init { fetchCategorias() }
}