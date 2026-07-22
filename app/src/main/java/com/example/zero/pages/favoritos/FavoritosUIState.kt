package com.example.zero.pages.favoritos

import com.example.zero.models.Favorito

sealed interface FavoritosUIState {
    data object Loading : FavoritosUIState
    data class Success(val favoritos: List<Favorito>) : FavoritosUIState
    data class Error(val message: String) : FavoritosUIState
}