package com.example.zero.pages.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zero.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminProductosViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<AdminProductosUIState>(AdminProductosUIState.Loading)
    val uiState: StateFlow<AdminProductosUIState> = _uiState.asStateFlow()

    var idProducto by mutableStateOf<String?>(null)
    var nombre by mutableStateOf("")
    var descripcion by mutableStateOf("")
    var precio by mutableStateOf("")
    var stock by mutableStateOf("")
    var talla by mutableStateOf("")
    var color by mutableStateOf("")
    var idCategoria by mutableStateOf("")

    fun fetchProductos() {
        viewModelScope.launch {
            _uiState.value = AdminProductosUIState.Loading
            try {
                val response = RetrofitClient.adminProductosService.getProductos()
                _uiState.value = AdminProductosUIState.Success(response.productos)
            } catch (e: Exception) {
                _uiState.value = AdminProductosUIState.Error("Error al cargar: ${e.localizedMessage}")
            }
        }
    }

    fun insertarProducto() {
        viewModelScope.launch {
            try {
                RetrofitClient.adminProductosService.insertarProducto(
                    nombre, descripcion, precio, stock, talla, color, idCategoria
                )
                fetchProductos()
            } catch (e: Exception) {
                _uiState.value = AdminProductosUIState.Error("Error al insertar: ${e.localizedMessage}")
            }
        }
    }

    fun actualizarProducto() {
        val id = idProducto ?: return
        viewModelScope.launch {
            try {
                RetrofitClient.adminProductosService.actualizarProducto(
                    id, nombre, descripcion, precio, stock, talla, color, idCategoria
                )
                fetchProductos()
            } catch (e: Exception) {
                _uiState.value = AdminProductosUIState.Error("Error al actualizar: ${e.localizedMessage}")
            }
        }
    }

    fun eliminarProducto() {
        val id = idProducto ?: return
        viewModelScope.launch {
            try {
                RetrofitClient.adminProductosService.eliminarProducto(id)
                fetchProductos()
            } catch (e: Exception) {
                _uiState.value = AdminProductosUIState.Error("Error al eliminar: ${e.localizedMessage}")
            }
        }
    }

    fun limpiarFormulario() {
        idProducto = null
        nombre = ""
        descripcion = ""
        precio = ""
        stock = ""
        talla = ""
        color = ""
        idCategoria = ""
    }

    init { fetchProductos() }
}