package com.example.damflix.data.model

data class Pelicula(
    val titulo: String,
    val director: String,
    val anio: String,
    val esVista: Boolean,
    val esFavorita: Boolean = false
)
