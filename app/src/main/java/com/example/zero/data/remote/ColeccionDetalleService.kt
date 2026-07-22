package com.example.zero.data.remote

import com.example.zero.models.ProductoCategoriaResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ColeccionDetalleService {
    @GET("productos_por_categoria.php")
    suspend fun getProductosPorCategoria(
        @Query("id_categoria") idCategoria: Int
    ): ProductoCategoriaResponse
}