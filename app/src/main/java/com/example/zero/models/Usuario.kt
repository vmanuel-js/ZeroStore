package com.example.zero.models

data class Usuario(
    val id_usuario: Int,
    val nombre_usuario: String,
    val apellidos_usuario: String? = null,
    val email_usuario: String? = null,
    val password: String? = null,
    val telefono: String? = null,
    val direccion: String? = null,
    val fecha_registro: String? = null,
    val rol: String? = null
)