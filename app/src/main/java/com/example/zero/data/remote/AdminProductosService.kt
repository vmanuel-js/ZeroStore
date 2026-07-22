package com.example.zero.data.remote

import com.example.zero.models.CrudResponse
import com.example.zero.models.ProductosResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AdminProductosService {

    @GET("productos.php")
    suspend fun getProductos(): ProductosResponse

    @FormUrlEncoded
    @POST("productos_insertar.php")
    suspend fun insertarProducto(
        @Field("nombre_producto") nombre: String,
        @Field("descripcion_producto") descripcion: String,
        @Field("precio") precio: String,
        @Field("stock") stock: String,
        @Field("talla") talla: String,
        @Field("color") color: String,
        @Field("id_categoria") idCategoria: String,
    ): CrudResponse

    @FormUrlEncoded
    @POST("productos_actualizar.php")
    suspend fun actualizarProducto(
        @Field("id_producto") idProducto: String,
        @Field("nombre_producto") nombre: String,
        @Field("descripcion_producto") descripcion: String,
        @Field("precio") precio: String,
        @Field("stock") stock: String,
        @Field("talla") talla: String,
        @Field("color") color: String,
        @Field("id_categoria") idCategoria: String,
    ): CrudResponse

    @FormUrlEncoded
    @POST("productos_eliminar.php")
    suspend fun eliminarProducto(
        @Field("id_producto") idProducto: String,
        ): CrudResponse

}