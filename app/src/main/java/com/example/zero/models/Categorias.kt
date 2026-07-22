package com.example.zero.models

import java.io.Serializable

data class Categorias(
    val id_categoria: Int,
    val nombre_categoria: String,
    val descripcion_categoria: String,
    val imagen_url: String,
): Serializable
