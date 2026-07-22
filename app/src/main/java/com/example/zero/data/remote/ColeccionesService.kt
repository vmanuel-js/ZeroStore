package com.example.zero.data.remote

import com.example.zero.models.CategoriasResponse
import retrofit2.http.GET

interface ColeccionesService {
    @GET("categorias.php")
    suspend fun getCategorias(): CategoriasResponse
}