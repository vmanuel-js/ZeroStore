package com.example.zero.pages.tienda

import com.example.zero.models.Tienda

sealed interface TiendaUIState {
    data object Loading: TiendaUIState

    data class Success (
        val tiendas : List<Tienda>
    ): TiendaUIState

    data class Error (
        val message: String
    ): TiendaUIState
}