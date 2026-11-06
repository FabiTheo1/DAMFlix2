package com.example.damflix.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "peliculas")
data class Pelicula(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val titulo: String,
    val director: String,
    val genero: String = "",
    val anio: String,
    val sinopsis: String = "",
    val esVista: Boolean = false,
    val esFavorita: Boolean = false
)
