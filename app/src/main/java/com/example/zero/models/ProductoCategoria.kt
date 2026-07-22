package com.example.zero.models

import java.io.Serializable

data class ProductoCategoria(
    val id_producto: Int,
    val id_categoria: Int,
    val nombre_producto: String,
    val descripcion_producto: String,
    val precio: Double,
    val stock: Int,
    val talla: String,
    val color: String,
    val url_imagen: String?
) : Serializable