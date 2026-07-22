package com.example.zero.pages.grafico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zero.data.remote.RetrofitClient
import com.example.zero.models.StockCategoria
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GraficoStockViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<GraficoStockUIState>(GraficoStockUIState.Loading)
    val uiState: StateFlow<GraficoStockUIState> = _uiState.asStateFlow()

    fun fetchStock() {
        viewModelScope.launch {
            _uiState.value = GraficoStockUIState.Loading
            try {
                val productos = RetrofitClient.productosService.getProductos().productos

                val stockPorCategoria = productos
                    .groupBy { it.nombre_categoria }
                    .map { (categoria, lista) ->
                        StockCategoria(
                            categoria  = categoria,
                            totalStock = lista.sumOf { it.stock }
                        )
                    }
                    .sortedByDescending { it.totalStock }

                val bajoStock = productos
                    .filter { it.stock <= 10 }
                    .sortedBy { it.stock }

                _uiState.value = GraficoStockUIState.Success(
                    datos              = stockPorCategoria,
                    productosBajoStock = bajoStock
                )
            } catch (e: Exception) {
                _uiState.value = GraficoStockUIState.Error("Error al cargar: ${e.localizedMessage}")
            }
        }
    }

    init { fetchStock() }
}