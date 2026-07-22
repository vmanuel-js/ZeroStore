package com.example.zero.data.remote

import com.example.zero.models.ProductosResponse
import retrofit2.http.GET

interface ProductosService {
    @GET("productos.php")
    suspend fun getProductos(): ProductosResponse
}