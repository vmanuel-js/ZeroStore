package com.example.zero.pages.initial

import com.example.zero.models.Categorias
import com.example.zero.models.Productos

data class InitialData(
    val categorias: List<Categorias>,
    val productos: List<Productos>,
    val mapaImagenes: Map<Int, String>,
    val mapaFavoritos: Set<Int>
)