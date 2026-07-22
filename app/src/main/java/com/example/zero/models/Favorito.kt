package com.example.zero.models

import java.io.Serializable

data class Favorito(
    val id_favorito: Int,
    val id_usuario: Int,
    val id_producto: Int,
    val fecha_agregado: String,
    val nombre_producto: String,
    val precio: Double,
    val color: String,
    val stock: Int,
    val nombre_categoria: String,
    val url_imagen: String?
) : Serializable

data class FavoritosResponse(
    val favoritos: List<Favorito>
)

data class FavoritoVerificarResponse(
    val es_favorito: Boolean
)