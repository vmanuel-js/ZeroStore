package com.example.zero.pages.mapa_general

import com.example.zero.models.Tienda

sealed interface MapaGeneralUIState {
    data object Loading: MapaGeneralUIState

    data class Success (
        val tiendas: List<Tienda>
    ) : MapaGeneralUIState

    data class Error (
        val message: String
    ) : MapaGeneralUIState
}