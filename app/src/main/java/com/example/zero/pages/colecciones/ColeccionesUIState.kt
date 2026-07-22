package com.example.zero.pages.colecciones

import com.example.zero.models.Categorias

sealed interface ColeccionesUIState {
    data object Loading : ColeccionesUIState
    data class Success(val categorias: List<Categorias>) : ColeccionesUIState
    data class Error(val message: String) : ColeccionesUIState
}