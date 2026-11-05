package com.example.damflix.data.model

data class Pelicula(
    val id: Long = 0,
    val titulo: String,
    val director: String,
    val genero: String = "",
    val anio: String,
    val sinopsis: String = "",
    val esVista: Boolean = false,
    val esFavorita: Boolean = false
)
