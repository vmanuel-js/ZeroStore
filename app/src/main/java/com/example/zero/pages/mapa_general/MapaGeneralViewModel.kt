package com.example.zero.pages.mapa_general

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zero.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapaGeneralViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<MapaGeneralUIState> (MapaGeneralUIState.Loading)
    val uiState : StateFlow<MapaGeneralUIState> = _uiState.asStateFlow()

    fun fetchTiendas () {
        viewModelScope.launch {
            _uiState.value = MapaGeneralUIState.Loading

            try {
                val response = RetrofitClient.tiendaService.getTiendas()
                _uiState.value = MapaGeneralUIState.Success(response.tiendas)
            } catch (e: Exception) {
                _uiState.value = MapaGeneralUIState.Error(e.localizedMessage ?: "Error")
            }
        }
    }

    init {
        fetchTiendas()
    }
}