package com.example.zero.models

import java.io.Serializable

data class Productos(
    val id_producto: Int,
    val nombre_producto: String,
    val descripcion_producto: String,
    val precio: Double,
    val stock: Int,
    val talla: String,
    val color: String,
    val activo: Int,
    val nombre_categoria: String,
    val id_categoria: Int
) : Serializable