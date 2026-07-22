package com.example.zero.pages.tienda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zero.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TiendaViewModel: ViewModel() {

    private val _uiState = MutableStateFlow<TiendaUIState>(TiendaUIState.Loading)
    val uiState: StateFlow<TiendaUIState> = _uiState.asStateFlow()

    fun fetchTiendas() {
        viewModelScope.launch {
            _uiState.value = TiendaUIState.Loading

            try {
                val response = RetrofitClient.tiendaService.getTiendas()
                _uiState.value = TiendaUIState.Success(response.tiendas)
            } catch (e: Exception) {
                _uiState.value = TiendaUIState.Error("Error al cargar las tiendas: ${e.localizedMessage}")
            }
        }
    }

    init { fetchTiendas() }
}