package com.example.damflix.data

import com.example.damflix.data.local.Pelicula
import com.example.damflix.data.local.PeliculaDao
import kotlinx.coroutines.flow.Flow

class PeliculaRepository(private val peliculaDao: PeliculaDao) {

    val peliculas: Flow<List<Pelicula>> = peliculaDao.obtenerTodas()

    suspend fun insertar(pelicula: Pelicula) {
        peliculaDao.insertar(pelicula)
    }

    suspend fun actualizar(pelicula: Pelicula) {
        peliculaDao.actualizar(pelicula)
    }

    suspend fun toggleFavorito(pelicula: Pelicula) {
        val actualizada = pelicula.copy(esFavorita = !pelicula.esFavorita)
        peliculaDao.actualizar(actualizada)
    }

    suspend fun eliminar(pelicula: Pelicula) {
        peliculaDao.eliminar(pelicula)
    }
}
