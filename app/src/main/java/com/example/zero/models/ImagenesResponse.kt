package com.example.zero.models

data class ImagenesResponse(val imagenes: List<ImagenProducto>)
data class ImagenProducto(val id_producto: Int, val url_imagen: String, val es_principal: Int)
