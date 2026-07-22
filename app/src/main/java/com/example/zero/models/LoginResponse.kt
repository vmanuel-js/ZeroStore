package com.example.zero.models

data class LoginResponse(
    val success: Boolean,
    val message: String? = null,
    val id_usuario: Int? = null,
    val nombre: String? = null,
    val rol: String? = null
)
