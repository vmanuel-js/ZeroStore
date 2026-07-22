package com.example.zero.pages.coleccion_detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zero.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ColeccionDetalleViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<ColeccionDetalleUIState>(ColeccionDetalleUIState.Loading)
    val uiState: StateFlow<ColeccionDetalleUIState> = _uiState.asStateFlow()
    fun fetchProductos(idCategoria: Int) {
        viewModelScope.launch {
            _uiState.value = ColeccionDetalleUIState.Loading
            try {
                val response = RetrofitClient.coleccionDetalleService.getProductosPorCategoria(idCategoria)
                _uiState.value = ColeccionDetalleUIState.Success(response.productos)
            } catch (e: Exception) {
                _uiState.value = ColeccionDetalleUIState.Error("Error al cargar productos: ${e.localizedMessage}")
            }
        }
    }
}