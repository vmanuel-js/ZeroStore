package com.example.zero.data.remote

import com.example.zero.models.ImagenesResponse
import retrofit2.http.GET

interface ImagenesService {
    @GET("imagenes.php")
    suspend fun getImagenes(): ImagenesResponse
}