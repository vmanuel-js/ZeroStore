package com.example.zero.models

import java.io.Serializable

data class Tienda(
    val id_tienda: Int,
    val nombre: String,
    val distrito: String,
    val descripcion: String,
    val imagen_url: String,
    val latitud: Double,
    val longitud: Double,
    val telefono: String?,
    val horario: String?
) : Serializable
