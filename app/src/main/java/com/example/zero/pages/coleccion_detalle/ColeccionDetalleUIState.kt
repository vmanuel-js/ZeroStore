package com.example.zero.pages.coleccion_detalle

import com.example.zero.models.ProductoCategoria

sealed interface ColeccionDetalleUIState {
    data object Loading : ColeccionDetalleUIState
    data class Success(val productos: List<ProductoCategoria>) : ColeccionDetalleUIState
    data class Error(val message: String) : ColeccionDetalleUIState
}