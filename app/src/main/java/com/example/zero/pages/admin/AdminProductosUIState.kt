package com.example.zero.pages.admin

import com.example.zero.models.Productos

sealed interface AdminProductosUIState {
    data object Loading : AdminProductosUIState
    data class Success(val productos: List<Productos>) : AdminProductosUIState
    data class Error(val message: String) : AdminProductosUIState
}