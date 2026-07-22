package com.example.zero.pages.grafico

import com.example.zero.models.Productos
import com.example.zero.models.StockCategoria

sealed interface GraficoStockUIState {
    data object Loading : GraficoStockUIState
    data class Success(
        val datos: List<StockCategoria>,
        val productosBajoStock: List<Productos>
    ) : GraficoStockUIState
    data class Error(val message: String) : GraficoStockUIState
}