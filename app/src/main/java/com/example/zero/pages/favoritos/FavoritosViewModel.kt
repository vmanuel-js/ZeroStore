package com.example.zero.pages.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zero.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritosViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<FavoritosUIState>(FavoritosUIState.Loading)
    val uiState: StateFlow<FavoritosUIState> = _uiState.asStateFlow()

    private val idUsuario get() = com.example.zero.utils.usuarioActivo?.id_usuario ?: 0
    fun fetchFavoritos() {
        viewModelScope.launch {
            _uiState.value = FavoritosUIState.Loading
            try {
                val favoritos = RetrofitClient.favoritosService.getFavoritos(idUsuario).favoritos
                _uiState.value = FavoritosUIState.Success(favoritos)
            } catch (e: Exception) {
                _uiState.value = FavoritosUIState.Error("Error al cargar favoritos: ${e.localizedMessage}")
            }
        }
    }

    fun toggleFavorito(idProducto: Int) {
        val estadoActual = _uiState.value
        if (estadoActual !is FavoritosUIState.Success) return

        viewModelScope.launch {
            try {
                val body = mapOf("id_usuario" to idUsuario, "id_producto" to idProducto)
                RetrofitClient.favoritosService.eliminarFavorito(body)
                val nuevaLista = estadoActual.favoritos.filter { it.id_producto != idProducto }
                _uiState.value = FavoritosUIState.Success(nuevaLista)
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    init { fetchFavoritos() }
}