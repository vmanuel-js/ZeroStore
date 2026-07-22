package com.example.zero.pages.initial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zero.data.Busqueda
import com.example.zero.data.BusquedaDao
import com.example.zero.data.remote.RetrofitClient
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InitialViewModel(private val busquedaDao: BusquedaDao) : ViewModel() {

    private val _uiState = MutableStateFlow<InitialUIState>(InitialUIState.Loading)
    val uiState: StateFlow<InitialUIState> = _uiState.asStateFlow()

    val busquedasRecientes: StateFlow<List<Busqueda>> = busquedaDao.obtenerRecientes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val idUsuario get() = com.example.zero.utils.usuarioActivo?.id_usuario ?: 0

    fun fetchTodo() {
        viewModelScope.launch {
            _uiState.value = InitialUIState.Loading
            try {
                val deferredImagenes   = async { RetrofitClient.imagenesService.getImagenes().imagenes }
                val deferredCategorias = async { RetrofitClient.categoriasService.getCategorias().categorias }
                val deferredProductos  = async { RetrofitClient.productosService.getProductos().productos }
                val deferredFavoritos  = async { RetrofitClient.favoritosService.getFavoritos(idUsuario).favoritos }

                val mapaImagenes  = deferredImagenes.await().filter { it.es_principal == 1 }.associate { it.id_producto to it.url_imagen }
                val mapaFavoritos = deferredFavoritos.await().map { it.id_producto }.toSet()

                _uiState.value = InitialUIState.Success(
                    InitialData(
                        categorias    = deferredCategorias.await(),
                        productos     = deferredProductos.await(),
                        mapaImagenes  = mapaImagenes,
                        mapaFavoritos = mapaFavoritos
                    )
                )
            } catch (e: Exception) {
                _uiState.value = InitialUIState.Error("Error al cargar: ${e.localizedMessage}")
            }
        }
    }

    fun guardarBusqueda(termino: String) {
        if (termino.isBlank()) return
        viewModelScope.launch {
            busquedaDao.insertar(Busqueda(termino = termino.trim()))
        }
    }

    fun eliminarBusqueda(termino: String) {
        viewModelScope.launch { busquedaDao.eliminar(termino) }
    }

    fun limpiarHistorial() {
        viewModelScope.launch { busquedaDao.limpiarTodo() }
    }

    fun toggleFavorito(idProducto: Int) {
        val estadoActual = _uiState.value
        if (estadoActual !is InitialUIState.Success) return
        viewModelScope.launch {
            try {
                val body = mapOf("id_usuario" to idUsuario, "id_producto" to idProducto)
                val esFavorito = idProducto in estadoActual.data.mapaFavoritos
                if (esFavorito) RetrofitClient.favoritosService.eliminarFavorito(body)
                else RetrofitClient.favoritosService.agregarFavorito(body)
                val nuevoMapa = if (esFavorito) estadoActual.data.mapaFavoritos - idProducto
                else estadoActual.data.mapaFavoritos + idProducto
                _uiState.value = InitialUIState.Success(estadoActual.data.copy(mapaFavoritos = nuevoMapa))
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    init { fetchTodo() }
}