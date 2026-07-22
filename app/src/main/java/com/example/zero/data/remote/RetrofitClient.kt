package com.example.zero.data.remote

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.zero.utils.API_URL
import retrofit2.create

object RetrofitClient {
    private val gson = GsonBuilder()
        .create()

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val categoriasService: CategoriasService by lazy { instance.create(CategoriasService::class.java) }
    val productosService: ProductosService by lazy { instance.create(ProductosService::class.java) }
    val imagenesService: ImagenesService by lazy { instance.create(ImagenesService::class.java) }
    val favoritosService: FavoritosService by lazy { instance.create(FavoritosService::class.java) }
    val loginService: LoginService by lazy { instance.create(LoginService::class.java)}
    val coleccionesService: ColeccionesService by lazy { instance.create(ColeccionesService::class.java) }
    val coleccionDetalleService: ColeccionDetalleService by lazy { instance.create(ColeccionDetalleService::class.java) }

    val adminProductosService: AdminProductosService by lazy { instance.create(AdminProductosService::class.java)  }

    val tiendaService: TiendaService by lazy {instance.create(TiendaService::class.java)}
    inline fun <reified T> create(): T = instance.create(T::class.java)
}