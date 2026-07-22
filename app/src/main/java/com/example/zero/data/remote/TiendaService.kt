package com.example.zero.data.remote

import com.example.zero.models.TiendaResponse
import retrofit2.http.GET

interface TiendaService {
    @GET("tiendas.php")
    suspend fun getTiendas(): TiendaResponse
}