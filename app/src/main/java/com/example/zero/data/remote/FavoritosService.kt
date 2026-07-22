package com.example.zero.data.remote

import com.example.zero.models.FavoritoVerificarResponse
import com.example.zero.models.FavoritosResponse
import okhttp3.ResponseBody
import retrofit2.http.*

interface FavoritosService {

    @GET("favoritos_listar.php")
    suspend fun getFavoritos(
        @Query("id_usuario") idUsuario: Int
    ): FavoritosResponse

    @Headers("Content-Type: application/json")
    @POST("favoritos_agregar.php")
    suspend fun agregarFavorito(
        @Body body: Map<String, Int>
    ): ResponseBody

    @Headers("Content-Type: application/json")
    @POST("favoritos_eliminar.php")
    suspend fun eliminarFavorito(
        @Body body: Map<String, Int>
    ): ResponseBody

    @GET("favoritos_verificar.php")
    suspend fun verificarFavorito(
        @Query("id_usuario") idUsuario: Int,
        @Query("id_producto") idProducto: Int
    ): FavoritoVerificarResponse
}